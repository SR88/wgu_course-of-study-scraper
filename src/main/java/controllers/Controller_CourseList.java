package controllers;

import entity.Course;
import entitymanagers.EntityManagement;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.CourseList;
import org.openqa.selenium.support.ui.ExpectedCondition;
import pages.SSO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By sethsneddon on Aug, 17 2018.
 */
public class Controller_CourseList {

    EntityManagement emCoS;
    SSO sso;
    WebDriver driver = null;
    WebDriverWait wait;
    ArrayList<String> credentials;


    public final String initialURL = "https://my.wgu.edu/courses/course-list";

    public Controller_CourseList() {
        this.emCoS = new EntityManagement();
        this.sso = new SSO();
    }

    public ExpectedCondition<Boolean> documentReady_Check(){
        return driver -> (Boolean) ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
    }


    public void courselist_ScrapeAll(WebDriver driver, WebDriverWait wait /*ArrayList<String> credentials*/){
//        WebDrivers webDrivers = new WebDrivers();
        this.driver = driver;
        this.wait = wait;
//        this.credentials = credentials;
//        courselist_SignIn();
        courselist_ScrapeAndInsertAllCourses();
    }


    public void courselist_SignIn(){
        driver.get(initialURL);

        wait.until(documentReady_Check());

        WebElement field_Login = driver.findElement(By.id("login-username"));
        field_Login.sendKeys(credentials.get(0));
        WebElement field_Password = driver.findElement(By.id("login-password"));
        field_Password.sendKeys(credentials.get(1));
        field_Password.sendKeys(Keys.ENTER);
    }


    public void courselist_ScrapeAndInsertAllCourses(){
        wait.until(documentReady_Check());

        WebElement nextButton = driver.findElement(By.xpath("//button[@aria-label=\"Next\"]"));

        System.out.println("FETCHING ALL COURSES...\r\r");

        while (nextButton.getAttribute("disabled") == null){
            courselist_ScrapeCourse();
            nextButton.click();
        }

        System.out.println("NUMBER OF COURSES IN COURSE LIST: " + CourseList.coursesList.size());

        emCoS.uploadCourses(CourseList.coursesList);   // this to upload via entity manager

        System.out.println();
        System.out.println("COMPLETED COMPILING COURSE OBJECTS");

        driver.quit();
    }


    public void courselist_ScrapeCourse(){
        WebElement baseTable = driver.findElement(By.xpath("//tbody"));
        List<WebElement> tableRows = baseTable.findElements(By.tagName("tr"));

        for (WebElement row : tableRows){
            Course course = new Course();
            //course.setSyncDate(); fix later
            List<WebElement> cells = row.findElements(By.tagName("td"));
            int i = 1;
            for ( WebElement cell : cells){
                if(i == 1){
                    course.setCosType(cell.getText());
                }
                if(i == 2){
                    course.setPamsVersion(Integer.parseInt(cell.getText()));
                }
                if(i == 3){
                    WebElement name = cell.findElement(By.xpath(".//a"));
                    course.setCosName(name.getText());
                    course.setUrl(name.getAttribute("href"));
                }
                if(i == 5){
                    course.setCosVersion(Integer.parseInt(cell.getText()));
                }
                if(i == 6){
                    course.setVersionId(cell.getText());
                }
                i++;
            }
            CourseList.coursesList.add(course);
        }
    }

}
