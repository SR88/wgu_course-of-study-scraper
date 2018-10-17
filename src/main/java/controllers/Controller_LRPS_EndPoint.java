package controllers;

import entity.EndPoint;
import entitymanagers.EntityManagement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * Created By sethsneddon on Oct, 09 2018.
 */
public class Controller_LRPS_EndPoint {

    String pageTitle;
    String providerUrl;
    String fullUrl;
    String lrps;
    WebDriver driver;
    WebDriverWait wait;
    EntityManagement em;

    public Controller_LRPS_EndPoint() {
        em = new EntityManagement();
    }

    public ExpectedCondition<Boolean> documentReady_Check(){
        return driver ->  (Boolean) ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
    }

    public void endPoint_Scrape(WebDriver driver, WebDriverWait wait, String distinct_LRPS_URL){
        this.driver = driver;
        this.wait = wait;
        this.lrps = distinct_LRPS_URL;

        wait.until(documentReady_Check());

        this.driver.get(distinct_LRPS_URL);

        System.out.println("DISTINCT URL :::: " +distinct_LRPS_URL);
        System.out.println(this.lrps);

        driver.manage().window().maximize();
        wait.until(documentReady_Check());

        navigateToEndPoint();
        buildEndPointDetails();

        // todo insert data into db
        EndPoint ep = new EndPoint(providerUrl, pageTitle, fullUrl, null, distinct_LRPS_URL);
        Long epRecordNumber = em.create_Distinct_LRPS_EndPoint(ep, lrps);

        // todo update where lrps equals for db
        em.links_SetEndpoint(distinct_LRPS_URL);

        try {
            Thread.sleep(37000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void navigateToEndPoint() {
        System.out.println("Attempting to get LRPS: " + lrps);
        driver.get(lrps);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("Unable to sleep thread when navigating to the endpoint");
        }
        wait.until(documentReady_Check());
    }

    private void buildEndPointDetails() {
        pageTitle = driver.getTitle();
        fullUrl = driver.getCurrentUrl();
        URI uri;
        try {
            uri = new URI(fullUrl);
            providerUrl = uri.getHost();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.out.println("Unable to get Host out of URI");
        }
    }

}
