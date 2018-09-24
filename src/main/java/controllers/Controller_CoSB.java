package controllers;

import entity.Course;
import entity.Link;
import entitymanagers.EntityManagerCoS;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created By sethsneddon on Aug, 24 2018.
 */
public class Controller_CoSB {

    EntityManagerCoS emCoS;
    ExpectedCondition<Boolean> jsLoaded;
    Course course;
    WebDriver driver;
    WebDriverWait wait;

    public Controller_CoSB() {
        this.emCoS = new EntityManagerCoS();
    }


    public ExpectedCondition<Boolean> documentReady_Check(){
        return driver -> (Boolean) ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
    }


    public void cosb_Scrape(Course c, WebDriver driver, WebDriverWait wait){
        this.driver = driver;
        this.wait = wait;
        this.course = c;
        cosb_ScrapeMainDiv(course);
    }

    public void cosb_ScrapeMainDiv(Course c){
        String cos_URL = c.getUrl();

        driver.get(cos_URL);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        wait.until(documentReady_Check());

        driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);

        // to be used for grabbing main div with course content
        List<WebElement> anchorTags = driver.findElements(By.xpath("//div[@class=\'col col--lg--8 vr--after vr--after--lg margin--bottom--30 ng-scope\']//*[@href]"));

        List<Link> links = new ArrayList<>();
        links.clear();
        Link link;
        for (WebElement element : anchorTags){
            link = new Link();
            link.setUrl(element.getAttribute("href"));
            link.setLinkText(element.getText());
            links.add(link);
        }

        emCoS.link_Insert(c.getId(), links);

        links.clear();
    }


//    public void cosb_ScrapeMainDiv(Long l){
//        String cos_URL = emCoS.cos_GetURL(l);
//
//        driver.get(cos_URL);
//
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        wait.until(documentReady_Check());
//
//        driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
//
//
//
//        // to be used for grabbing main div with course content
//        List<WebElement> anchorTags = driver.findElements(By.xpath("//div[@class=\'col col--lg--8 vr--after vr--after--lg margin--bottom--30 ng-scope\']//*[@href]"));
//
//        List<Link> links = new ArrayList<>();
//        links.clear();
//        Link link;
//        for (WebElement element : anchorTags){
//            link = new Link();
//            link.setUrl(element.getAttribute("href"));
//            link.setLinkText(element.getText());
//            links.add(link);
//        }
//
//        emCoS.link_Insert(l, links);
//
//        links.clear();
//    }


    @Deprecated
    public void cosb_Scrape(List<Long> cosBs){
//        cosb_SignIn((long) 1);
        for (Long l : cosBs) {
//            cosb_ScrapeMainDiv(l);
        }
        driver.quit();
    }

    @Deprecated
    public void cosb_SignIn(Long l){
        String demo = "https://my.wgu.edu/courses/course/10020020";
        driver.get(demo);
        wait.until(jsLoaded);

        WebElement field_Login = driver.findElement(By.id("login-username"));
        field_Login.sendKeys("abcd");
        WebElement field_Password = driver.findElement(By.id("login-password"));
        field_Password.sendKeys("123");
        WebElement button_Login = driver.findElement(By.xpath("//button[@class=\"btn btn--primary box__item flex-none\"]"));
        button_Login.click();

        wait.until(jsLoaded);
    }
}
