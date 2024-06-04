package site;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
//import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MainTest {

    private WebDriver driver;

    @BeforeEach
    void setUp(){
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
    }

    @AfterEach
    void tearDown(){
        WebDriverManager.firefoxdriver().quit();
    }

    @Test
    @DisplayName("Should access power register page")
    void shouldAccessPowerRegisterPage() throws InterruptedException{
        driver.get("https://site-tc1.vercel.app/");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement link = wait.until(ExpectedConditions.
                elementToBeClickable(new By.ByXPath("/html/body/div/body/div/header/nav/ul/li/a")));
        link.click();
        final WebElement element = driver.
                findElement(By.xpath("/html/body/div/body/div[2]/div/div/form/div[1]/label"));
        assertThat(element.getText()).isEqualTo("Nome do Poder:");
        driver.close();

    }
}