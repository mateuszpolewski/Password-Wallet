package pl.application.passwordwallet.controller;

import org.testng.annotations.*;
import org.openqa.selenium.By;

@Test(testName = "test3")
public class ChangePasswordTest extends MyWebDriver{
    @Test
    public void shouldChangePassword() throws InterruptedException {
        driver.findElement(By.linkText("Zmień hasło")).click();
        driver.findElement(By.id("firstPassword")).sendKeys("NewPassword123");
        driver.findElement(By.id("secondPassword")).sendKeys("NewPassword123");
        driver.findElement(By.id("submitBtn")).click();
    }
}
