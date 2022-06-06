package pl.application.passwordwallet.controller;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(testName = "test2")
public class AddPasswordTest extends MyWebDriver {

    @Test(priority = 0)
    public void shouldAddPassword() throws InterruptedException {
        driver.findElement(By.id("addPasswordBtn")).click();
        driver.findElement(By.id("password")).sendKeys("Qwerty");
        driver.findElement(By.id("siteName")).sendKeys("facebook");
        driver.findElement(By.id("link")).sendKeys("https://www.facebook.com/");
        driver.findElement(By.id("submitBtn")).click();

        Thread.sleep(300);

        driver.findElement(By.id("backBtn")).click();
        System.out.println("Password added to database..");

    }

    @Test(priority = 1)
    public void shouldViewPasswords() {
        driver.findElement(By.linkText("Wyświetl hasła")).click();
        driver.findElement(By.linkText("Wyświetl hasło")).click();
        driver.findElement(By.linkText("Powrót")).click();
    }
}
