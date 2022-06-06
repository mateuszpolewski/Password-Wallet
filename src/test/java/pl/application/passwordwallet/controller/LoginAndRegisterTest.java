package pl.application.passwordwallet.controller;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.*;

@Test(testName = "test1")
public class LoginAndRegisterTest extends MyWebDriver {

    @BeforeClass
    public void clickOnRegisterLink() {
        driver.findElement(By.linkText("Rejestracja")).click();
    }

    @Test
    public void verifyHomepageTitle() {
        Assert.assertEquals(driver.getTitle(), "Portfel haseł");
    }

    @Test(priority = 0)
    public void shouldRegisterUser() throws InterruptedException {
        driver.findElement(By.id("name")).sendKeys("Mateusz");
        driver.findElement(By.id("lastName")).sendKeys("Polewski");
        driver.findElement(By.id("email")).sendKeys("mateusszz@abcd.com");
        driver.findElement(By.id("password")).sendKeys("NewPassword123");
        driver.findElement(By.id("submitBtn")).click();

        Thread.sleep(300);

        driver.findElement(By.id("backBtn")).click();
        System.out.println("Trying to create existing user");
    }

    @Test(priority = 1)
    public void shouldNotGetLoggedIn() {
        driver.findElement(By.className("btn")).click();
        System.out.println("Trying to login with empty fields");
    }

    @Test(priority = 2)
    public void shouldGetLoggedIn() {
        WebElement email=driver.findElement(By.id("email"));
        WebElement password=driver.findElement(By.id("password"));
        email.sendKeys("mateusszz@abcd.com"); //"mar@gmail.com"
        password.sendKeys("NewPassword123");//"NewPassword123"
        driver.findElement(By.className("btn")).click();
        System.out.println("User logged in");
    }

}
