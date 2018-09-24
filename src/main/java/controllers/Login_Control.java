package controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created By sethsneddon on Sep, 07 2018.
 */
public class Login_Control implements ILogin_Control {

    private String userName;
    private String password;
    private ArrayList<String> payload_Login;
    private JsonObject json_Login;
    private final static String INITIAL_URL = "https://my.wgu.edu/courses/course-list/";

    public ArrayList<String> get_credentials(){
        try {
            declare_LoginFilePath();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return payload_Login;
    }

    @Override
    public void declare_LoginFilePath() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter in relative path for Login File: ");
        String relativeLocation = scanner.next();

        BufferedReader reader = null;

        StringBuffer jsonPayloadStringBuffer = new StringBuffer();

        while (reader == null) {

            try {

                reader = new BufferedReader(new FileReader(relativeLocation));

            } catch (FileNotFoundException e) {

                e.printStackTrace();
                System.out.println("Your file was not found. Please try again. Remember you need the full path of the file.");

            } finally {

                String line;
                while ((line = reader.readLine()) != null){
                    jsonPayloadStringBuffer.append(line);
                    jsonPayloadStringBuffer.append("\n");
                }

                Object obj = new JsonParser().parse(String.valueOf(jsonPayloadStringBuffer));

                parse_LoginInformation(obj);
            }
        }
    }

    @Override
    public void parse_LoginInformation(Object obj) {
        setJson_Login((JsonObject) obj);
        StringBuffer sb = new StringBuffer();
        sb.append(String.valueOf(json_Login.get("username")));
        int i = sb.length();
        setUserName(sb.substring(2,i-2));
        sb = new StringBuffer();
        sb.append(String.valueOf(json_Login.get("password")));
        i = sb.length();
        setPassword(sb.substring(2,i-2));
        pass_LoginInformation();
    }

    @Override
    public void pass_LoginInformation() {
        payload_Login = new ArrayList<>();
        payload_Login.add(userName);
        payload_Login.add(password);
    }

    @Override
    public void login(WebDriver driver, WebDriverWait wait) {
        driver.get(INITIAL_URL);

        wait.until(documentReady_Check());

        WebElement field_Login = driver.findElement(By.id("login-username"));
        field_Login.sendKeys(payload_Login.get(0));
        WebElement field_Password = driver.findElement(By.id("login-password"));
        field_Password.sendKeys(payload_Login.get(1));
        field_Password.sendKeys(Keys.ENTER);
    }

    public ExpectedCondition<Boolean> documentReady_Check(){
        return driver -> (Boolean) ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
    }


    @Override
    public String toString() {
        return "Login_Control{" +
                "payload_Login=" + payload_Login +
                '}';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<String> getPayload_Login() {
        return payload_Login;
    }

    public void setPayload_Login(ArrayList<String> payload_Login) {
        this.payload_Login = payload_Login;
    }

    public JsonObject getJson_Login() {
        return json_Login;
    }

    public void setJson_Login(JsonObject json_Login) {
        this.json_Login = json_Login;
    }
}
