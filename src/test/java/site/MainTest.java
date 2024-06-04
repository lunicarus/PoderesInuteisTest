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
    @DisplayName("Should provide error message if login and password are empty")
    void shouldProvideErrorMessageIfLoginAndPasswordAreEmpty() {
        driver.get("https://site-tc1.vercel.app/");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(By.tagName("a")));

        link.click();
        driver.close();

    }
    @Test
    @DisplayName("Should get the search bar from Google")
    void shouldGetTheSearchBarFromGoogle() {
        driver.get("https://www.google.com");
        final WebElement searchBar = driver.findElement(By.className("gLFyf"));
        final String accessibleName = searchBar.getAccessibleName();
        System.out.println(accessibleName);
    }
}