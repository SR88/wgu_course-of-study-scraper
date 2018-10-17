package utility.content.provider.exceptions;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utility.ClickElement;

/**
 * Created By sethsneddon on Oct, 15 2018.
 */
public class VitalSourceHandling {

    private static final String EXCEPTION_URL_CASE1 = "https://bc.vitalsource.com/tenants/4/books/book";
    private String currentURL;
    private ClickElement clickElement;
    private WebDriver driver;

    public VitalSourceHandling() {
        clickElement = new ClickElement();
    }

    public void verifyURL(WebDriver driver){
        this.driver = driver;
        currentURL = driver.getCurrentUrl();
        if (currentURL.contentEquals(EXCEPTION_URL_CASE1)){
            clickContinue();
            enterEmail();
            registerEmail();
        }
    }

    private void registerEmail() {
        WebElement button_SubmitEmail;
        try{
            button_SubmitEmail = driver.findElement(By.xpath("//input[@id='submit']"));
            button_SubmitEmail.click();
        } catch (NoSuchElementException nse) {
            System.out.println("Vital Source Exception Handling :::: No \"Submission\" button for email registration is present");
        }
    }

    private void enterEmail() {
        WebElement input_Email;
        try {
            input_Email = driver.findElement(By.xpath("//input[@id='email-field']"));
            input_Email.sendKeys("seth.sneddon@wgu.edu");
        } catch (NoSuchElementException nse) {
            System.out.println("Vital Source Exception Handling :::: No \"Email Field\" is present");
        }
    }

    private void clickContinue() {
        // WebElement continueButton = null;
        try{
            clickElement.byXpath_Click("//span[contains(text(),\"Continue\")]", this.driver);
        } catch (NoSuchElementException nse) {
            System.out.println("Vital Source Exception Handling :::: No \"Continue\" button is present");
        }
    }


}
