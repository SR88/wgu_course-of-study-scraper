package controllers;

import org.openqa.selenium.WebDriver;
import webdrivers.WebDrivers;

/**
 * Created By sethsneddon on Sep, 17 2018.
 */
public class Driver_Controller implements IDriver_Controller {


    @Override
    public WebDrivers createWebDriver() {
        WebDrivers webDrivers = new WebDrivers();
        webDrivers.initializeFireFox(webDrivers);
        return webDrivers;
    }

    @Override
    public void closeDriver() {
        // todo unused, may be un-needed
    }
}
