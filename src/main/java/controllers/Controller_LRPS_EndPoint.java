package controllers;

import entity.EndPoint;
import entitymanagers.EntityManagement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
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
    static Boolean erroredSite = false;
    Boolean dnsFailure = false;


    public Controller_LRPS_EndPoint() {
        em = new EntityManagement();
    }

    private static Boolean apply(WebDriver driver) {
        Object result;
        try {
            result = ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
        } catch (UnhandledAlertException uae) {
            result =  Boolean.TRUE;
            erroredSite = true;
        }
        return (Boolean) result;

    }

    public ExpectedCondition<Boolean> documentReady_Check(){

        return Controller_LRPS_EndPoint::apply;

    }

    public void endPoint_Scrape(WebDriver driver, WebDriverWait wait, String distinct_LRPS_URL){
        this.driver = driver;
        this.wait = wait;
        this.lrps = distinct_LRPS_URL;

        wait.until(documentReady_Check());

        try {
            this.driver.get(distinct_LRPS_URL);
        } catch (WebDriverException wde) {
            System.out.println("DNS is unable to locate service");
            dnsFailure = true;
        }

        if (!dnsFailure) {

            System.out.println("DISTINCT URL :::: " + distinct_LRPS_URL);
            System.out.println(this.lrps);

            driver.manage().window().maximize();
            wait.until(documentReady_Check());

            navigateToEndPoint();

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            buildEndPointDetails();

            if (!erroredSite) {
                // todo insert data into db
                EndPoint ep = new EndPoint(providerUrl, pageTitle, fullUrl, null, distinct_LRPS_URL);
                Long epRecordNumber = em.create_Distinct_LRPS_EndPoint(ep, lrps);

                // todo update where lrps equals for db
                em.links_SetEndpoint(distinct_LRPS_URL);
            } else {
                EndPoint ep = new EndPoint("OOPS, this record is in error", "Please manually inspect this distinct url", "OOPS, this record is in error", null, distinct_LRPS_URL);
                Long epRecordNumber = em.create_Distinct_LRPS_EndPoint(ep, lrps);
                em.links_SetEndpoint(distinct_LRPS_URL);
            }
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
