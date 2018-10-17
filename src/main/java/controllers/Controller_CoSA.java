package controllers;

import entity.Course;
import entity.Link;
import entitymanagers.EntityManagement;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.SSO;
import utility.ClickElement;
import utility.PopupBlocker;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * Created By sethsneddon on Aug, 24 2018.
 */
public class Controller_CoSA {

    PopupBlocker popupBlocker;
    EntityManagement emCoS;
    SSO sso;
    WebElement button_Next;
    List<WebElement> anchorTags;
    ClickElement clickElement;
    ClickElement clickIntro;
    List<Link> links = new ArrayList<>();
    List<Long> list_FailedCourses = new ArrayList<>();
    WebDriver driver = null;
    WebDriverWait wait = null;
    Predicate<WebDriver> loaded;
    Course course;

    public Controller_CoSA() {
        this.emCoS = new EntityManagement();
        this.sso = new SSO();
        popupBlocker = new PopupBlocker();
        clickElement = new ClickElement();
        clickIntro = new ClickElement();
    }

    public void cosa_Scrape(Course c, WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.course = c;
        cosa_ScrapeMainDiv(course);
    }


    public void cosa_ScrapeMainDiv(Course c){
        driver.manage().window().maximize();
        wait.until(documentReady_Check());
        int nextButtonClicks = 0;
        String cos_URL = c.getUrl();
        driver.get(cos_URL);
        links.clear();

        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        popupBlocker.checkForPopup(driver);

        wait.until(documentReady_Check());
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        clickIntroductionButton();

        goToStartOfCourse();

        /** may change from time to time, need to check back periodically and update as COSA sites update */
        //WebElement mainContent = driver.findElement(By.xpath("//div[@class=\"study-plan-a__container__layout__center study-plan-a__container__layout__center_-left-expanded study-plan-a__container__layout__center_-right-expanded\"]/div[3]"));
        /*
            cos container plus links at top
            "//div[@class="study-plan-a__container__layout__center study-plan-a__container__layout__center_-left-expanded study-plan-a__container__layout__center_-right-expanded"]"

            next button
            "//button[contains(text(),'Next')]"

            //div[@class="study-plan-a__container__layout__center study-plan-a__container__layout__center_-left-expanded study-plan-a__container__layout__center_-right-expanded"]/div[3]

         */

        button_Next = getNextButton();

        Boolean isPresent = true;

        anchorTags = new ArrayList<>();

        while (isPresent) {
            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

            button_Next = getNextButton();

            try {
                button_Next = driver.findElement(By.xpath("//button[contains(text(),'Next')]"));
            } catch (NoSuchElementException e) {
                System.out.println("No next button found");
                isPresent = false;
            }
            // driver.findElements(By.xpath("//div[@class=\"study-plan-a__container__layout__center study-plan-a__container__layout__center_-left-expanded study-plan-a__container__layout__center_-right-expanded\"]/div[3]//*[@href]")
//            List<WebElement> children = driver.findElements(By.xpath("//div[@class=\"study-plan-a__container__layout__center study-plan-a__container__layout__center_-left-expanded study-plan-a__container__layout__center_-right-expanded\"]/div[3]//*[@href]"));

            anchorTags.addAll(driver.findElements(By.xpath("//div[@class=\"study-plan-a__container__layout__center study-plan-a__container__layout__center_-left-expanded study-plan-a__container__layout__center_-right-expanded\"]/div[3]//*[@href]")));

            for(WebElement e : anchorTags){
                System.out.println("Attempting to build link");
                Link link = new Link();
                link.setUrl(e.getAttribute("href"));
                link.setLinkText(e.getText());
                links.add(link);
            }

            anchorTags.clear();

            if (!hiddenButton_Exists() || isPresent == false) {
                break;
            } else {
                clickElement.byXpath_Click("//button[contains(text(),'Next')]", driver);
                nextButtonClicks ++;
            }

            if(nextButtonClicks > 40){
                break;
            }
        }

        if(links.size() > 0){
            emCoS.link_Insert(c.getId(), links);
        }
    }


    public void goToStartOfCourse(){

        int i = 0;

        do {
            if(previousSubjectPresent()) {
                clickPreviousSubject();
                i++;
            }
            if(previousTopicPresent()){
                clickPreviousTopic();
                i++;
            }
            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        } while ((previousSubjectPresent() || previousTopicPresent()) && !(i >= 25));

        if(i >= 25) {
            driver.navigate().refresh();
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        }
    }

    public ExpectedCondition<Boolean> documentReady_Check(){
        return driver -> (Boolean) ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
    }

    private boolean hiddenButton_Exists(){
        try{
            driver.findElement(By.xpath("//button[@class='btn--primary ng-hide']"));
        } catch (NoSuchElementException e) {
            return true;
        }
        return false;
    }

    public WebElement getNextButton(){
        WebElement nextButton = null;
        try{
            nextButton = driver.findElement(By.xpath("//button[contains(text(),'Next')]"));
        } catch (NoSuchElementException nse) {
            System.out.println("No button with the value \"Next\" was found");
        }
        return nextButton;
    }

    public void clickIntroductionButton(){
        // WebElement introductionButton = null;
        try{
            clickIntro.byXpath_Click("//button[contains(text(),'Introduction')]", this.driver);
        } catch (NoSuchElementException nse) {
            System.out.println("No \"Introduction\" was located in: " + this.course.toString());
//            WebElement previousButton;
//            do{
//                clickPreviousSubject();
//                previousButton = driver.findElement(By.xpath("//button/span/following-sibling::text()[contains(.,'Previous')]"));
//            } while (previousButton != null);
        }
    }

    public boolean previousSubjectPresent(){
        WebElement previousButton = null;
        boolean isPresent = true;
        try{
            previousButton = driver.findElement(By.xpath("//button[@data-track-courses-button-name=\"previous Subject\"]"));
        } catch (NoSuchElementException nse) {
            isPresent = false;
        }
        return isPresent;
    }

    public boolean previousTopicPresent(){
        WebElement previousButton = null;
        boolean isPresent = true;
        try{
            previousButton = driver.findElement(By.xpath("//button[@data-track-courses-button-name=\"previous Topic\"]"));
        } catch (NoSuchElementException nse) {
            isPresent = false;
        }
        return isPresent;
    }

    public void clickPreviousSubject(){
        WebElement previousButton = null;
        try{
            ClickElement clickPrevious = new ClickElement();
            // //button[@data-track-courses-button-name="previous Subject"]
            // //button/span/following-sibling::text()[contains(.,'Previous')]
            clickPrevious.byXpath_Click("//button[@data-track-courses-button-name=\"previous Subject\"]", this.driver);
        } catch (NoSuchElementException nse) {
            System.out.println("Unable to find previous subject button \'clickPreviousSubject()\'");
        }
    }

    public void clickPreviousTopic(){
        try{
            ClickElement clickPrevious = new ClickElement();
            clickPrevious.byXpath_Click("//button[@data-track-courses-button-name=\"previous Topic\"]", this.driver);
        } catch (java.util.NoSuchElementException nse) {
            System.out.println("Unable to find previous topic button \'clickPreviousTopic()\'");
        }
    }

}
