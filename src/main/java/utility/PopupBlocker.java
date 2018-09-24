package utility;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import webdrivers.WebDrivers;

/**
 * Created By sethsneddon on Aug, 28 2018.
 */
public class PopupBlocker {

    Actions a;

    public void checkForPopup(WebDriver driver){
        try{
            WebElement element = driver.findElement(By.xpath("//a[@class=\'wisepop-close\']"));
            closePopup(element, driver);
        } catch (NoSuchElementException e) {
        } finally {}
    }

    public void closePopup(WebElement element, WebDriver driver){
        a = new Actions(driver);
        a.click(element).build().perform();
    }

}
