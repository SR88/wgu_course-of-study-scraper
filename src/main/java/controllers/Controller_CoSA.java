package controllers;

import entity.Course;
import entity.Link;
import entitymanagers.EntityManagerCoS;
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
    EntityManagerCoS emCoS;
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
        this.emCoS = new EntityManagerCoS();
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

        //WebElement introductionButton = driver.findElement(By.xpath("//button[contains(text(),'Introduction')]"));
        // todo change to click element
        //introductionButton.click();


        //clickIntro.byXpath_Click("//button[contains(text(),'Introduction')]", this.driver);


        driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);

        clickIntroductionButton();

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

        Boolean isPresent;


        isPresent = true;

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

            if(nextButtonClicks > 50){
                break;
            }
        }

        if(links.size() > 0){
            emCoS.link_Insert(c.getId(), links);
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
        WebElement introductionButton = null;
        try{
            clickIntro.byXpath_Click("//button[contains(text(),'Introduction')]", this.driver);
        } catch (NoSuchElementException nse) {
            System.out.println("No \"Introduction\" was located in this course.");
            // todo write method for clicking previous until previous button == null or is hidden
        }
    }


    @Deprecated
    public void cosa_SignIn(ArrayList<String> credentials){

        String cos_Url = "https://my.wgu.edu/courses/course/10020020";
        driver.get(cos_Url);
        wait.until(documentReady_Check());

        WebElement field_Login = driver.findElement(By.id("login-username"));
        field_Login.sendKeys(credentials.get(0));
        WebElement field_Password = driver.findElement(By.id("login-password"));
        field_Password.sendKeys(credentials.get(1));
        WebElement button_Login = driver.findElement(By.xpath("//button[@class=\"btn btn--primary box__item flex-none\"]"));
        button_Login.click();

        wait.until(documentReady_Check());
    }

    @Deprecated
    public void cosa_ScrapeMainDiv(Long cos_Record){
        int nextButtonClicks = 0;
        String cos_URL = emCoS.cos_GetURL(cos_Record);
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

        /** may change from time to time, need to check back periodically and update as COSA sites update */
        WebElement mainContent = driver.findElement(By.xpath("//div[@class=\"study-plan-a__container__layout__center study-plan-a__container__layout__center_-left-expanded study-plan-a__container__layout__center_-right-expanded\"]/div[3]"));

        /*
            cos container plus links at top
            "//div[@class="study-plan-a__container__layout__center study-plan-a__container__layout__center_-left-expanded study-plan-a__container__layout__center_-right-expanded"]"

            next button
            "//button[contains(text(),'Next')]"

            //div[@class="study-plan-a__container__layout__center study-plan-a__container__layout__center_-left-expanded study-plan-a__container__layout__center_-right-expanded"]/div[3]

         */
        button_Next = driver.findElement(By.xpath("//button[contains(text(),'Next')]"));
        anchorTags = new ArrayList<>();

        Boolean isPresent = true;

        while (isPresent) {
            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
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

            if(nextButtonClicks > 50){
                break;
            }
        }

        if(links.size() > 0){
            emCoS.link_Insert(cos_Record, links);
        }
    }

//    @Deprecated
//    public void cosa_Scrape(List<Long> cosAs, ArrayList<String> credentials){
//        WebDrivers webDrivers = new WebDrivers();
//        driver = webDrivers.init();
//        wait = new WebDriverWait(driver, 20);
//
//        cosa_SignIn(credentials);
//        for (Long l : cosAs){
//            try {
//                cosa_ScrapeMainDiv(l);
//            } catch (Exception e) {
//                list_FailedCourses.add(l);
//            } finally {}
//        }
//
//        System.out.println("Courses Failed:");
//        for (Long l : list_FailedCourses){
//            System.out.println(l);
//        }
//        System.out.println("End of list");
//
//        this.driver.quit();
//    }
}
