package webdrivers;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.concurrent.TimeUnit;

/**
 * Created By sethsneddon on Aug, 17 2018.
 */
public class WebDrivers {

    public WebDriver driver = null;
    public HtmlUnitDriver driverH = null;
    public JavascriptExecutor jse = null;
    public WebDriverWait wait = null;
    public WebDriverWait waitH = null;


    // HTMLUnit Driver
    public void initH(){
        if(driverH == null){
            DesiredCapabilities capabilities = DesiredCapabilities.htmlUnit();
            capabilities.setVersion(BrowserType.CHROME);
            //System.setProperty("org.apache.commons.loggin.simplelog.defaultlog", "fatal");
            driverH = new HtmlUnitDriver(capabilities);
            jse = driverH;
            waitH = new WebDriverWait(driverH, 20);

        }
    }

    // Firefox Driver
    public WebDriver init(){
        if(driver == null) {
            FirefoxOptions opts = new FirefoxOptions();
            opts.addArguments("-private");
            System.setProperty("webdriver.gecko.driver", "geckodriver");
            driver = new FirefoxDriver(opts);
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            jse = (JavascriptExecutor)driver;
            wait = new WebDriverWait(driver, 20);
        }
        return driver;
    }

    public void initializeFireFox(WebDrivers webDrivers){
        FirefoxOptions opts = new FirefoxOptions();
        opts.addArguments("-private");

        FirefoxProfile profile = new FirefoxProfile();
//        profile.setPreference("browser.download.manager.showWhenStarting", false);
//        profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
//                "text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml");
//        profile.setPreference("browser.download.folderList", 1);
//        profile.setPreference("browser.helperApps.alwaysAsk.force", false);
//        profile.setPreference("browser.privatebrowsing.autostart", true);
        profile.setPreference("browser.download.alertOnEXEOpen", false);
        profile.setPreference("browser.helperApps.neverAsksaveToDisk", "application/x-msexcel,application/excel,application/x-excel,application/excel,application/x-excel,application/excel,application/vnd.ms-excel,application/x-excel,application/x-msexcel");
        profile.setPreference("browser.download.manager.showWhenStarting", false);
        profile.setPreference("browser.download.manager.focusWhenStarting", false);
        profile.setPreference("browser.helperApps.alwaysAsk.force", false);
        profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
        profile.setPreference("browser.download.manager.closeWhenDone", false);
        profile.setPreference("browser.download.manager.showAlertOnComplete", false);
        profile.setPreference("browser.download.manager.useWindow", false);
        profile.setPreference("browser.download.manager.showWhenStarting", false);
        profile.setPreference("services.sync.prefs.sync.browser.download.manager.showWhenStarting", false);
        profile.setPreference("pdfjs.disabled", true);

        opts.setCapability(FirefoxDriver.PROFILE, profile);

        System.setProperty("webdriver.gecko.driver", "geckodriver");

        driver = new FirefoxDriver(opts);

        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        jse = (JavascriptExecutor)driver;
        wait = new WebDriverWait(driver, 20);
    }



    public WebDrivers() {
    }

    public WebDrivers(WebDriver driver, JavascriptExecutor jse, WebDriverWait wait) {
        this.driver = driver;
        this.jse = jse;
        this.wait = wait;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public HtmlUnitDriver getDriverH() {
        return driverH;
    }

    public void setDriverH(HtmlUnitDriver driverH) {
        this.driverH = driverH;
    }

    public JavascriptExecutor getJse() {
        return jse;
    }

    public void setJse(JavascriptExecutor jse) {
        this.jse = jse;
    }

    public WebDriverWait getWait() {
        return wait;
    }

    public void setWait(WebDriverWait wait) {
        this.wait = wait;
    }

    public WebDriverWait getWaitH() {
        return waitH;
    }

    public void setWaitH(WebDriverWait waitH) {
        this.waitH = waitH;
    }
}
