package utility;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import webdrivers.WebDrivers;



/**
 * Created By sethsneddon on Aug, 27 2018.
 */
public class ClickElement {

    Actions a;
    public JavascriptExecutor jse = null;
    private WebElement element;

    public void byXpath_Click(String s, WebDriver driver){

        jse = (JavascriptExecutor)driver;

        a = new Actions(driver);

        element = driver.findElement(By.xpath(s));

        jse.executeScript(
                "arguments[0].scrollIntoView();", element
        );

        a.click(element).build().perform();
    }

}
