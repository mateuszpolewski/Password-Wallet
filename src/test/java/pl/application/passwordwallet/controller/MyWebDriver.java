package pl.application.passwordwallet.controller;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

public class MyWebDriver {
    public static String baseUrl = "http://localhost:8080/";
    public static WebDriver driver;

    @BeforeSuite
    public void launchBrowser() {
        System.setProperty("webdriver.gecko.driver","C:\\Program Files\\geckodriver-v0.30.0-win64\\geckodriver.exe");
        driver = new FirefoxDriver();
        driver.get(baseUrl);
    }

    @AfterSuite
    public void endSession() {
        driver.quit();
    }

}
