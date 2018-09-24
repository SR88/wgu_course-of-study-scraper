package controllers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created By sethsneddon on Sep, 07 2018.
 */
public interface ILogin_Control {

    ArrayList<String> get_credentials();

    void declare_LoginFilePath() throws IOException;

    void parse_LoginInformation(Object object);

    void pass_LoginInformation();

    void login(WebDriver driver, WebDriverWait wait);

}
