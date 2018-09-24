package controllers;

import webdrivers.WebDrivers;

/**
 * Created By sethsneddon on Sep, 17 2018.
 */
public interface IDriver_Controller {

    WebDrivers createWebDriver();

    void closeDriver();
}