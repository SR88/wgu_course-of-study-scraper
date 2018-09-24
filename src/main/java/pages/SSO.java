package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.Locale;
import java.util.ResourceBundle;



/**
 * Created By sethsneddon on Aug, 17 2018.
 */
public class SSO {

    public static final String PAGE_TITLE = "WGU Student Portal - Login";

    @FindBy(id = "login-username")
    WebElement field_Login;

    @FindBy(id = "login-password") WebElement field_Password;

    @FindBy(className = "btn btn--primary box__item flex-none") WebElement button_Login;


    public SSO(){
//        PageFactory.initElements(driverH, this);
    }

    public void setField_Login(String s){
        field_Login.sendKeys(s);
    }

    public void setField_Password(String s){
        field_Password.sendKeys(s);
    }

    public void click_Login(){
        button_Login.click();
    }

}
