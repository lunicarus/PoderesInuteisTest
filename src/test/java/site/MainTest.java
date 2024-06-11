package site;

import com.github.javafaker.Faker;
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
    private Faker faker;

//    @BeforeEach
//    void setUp(){
//        WebDriverManager.firefoxdriver().setup();
//        driver = new FirefoxDriver();
//    }
//
//    @AfterEach
//    void tearDown(){
//        WebDriverManager.firefoxdriver().quit();
//    }

    @Nested
    class HomePageTest{

        HomePage homePage;

        @BeforeEach
        void setUp(){
            homePage = new HomePage(driver);
        }
        @Test
        @DisplayName("Should access power register page")
        void shouldAccessPowerRegisterPage() throws InterruptedException{
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement link = wait.until(ExpectedConditions.
                    elementToBeClickable(homePage.getCadastrarButtom()));
            link.click();

            driver.close();

        }
    }

    @Nested
    class CRUDtests {
        @BeforeEach
        void setUp() {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
            driver.get("https://site-tc1.vercel.app/");
            faker = new Faker();
        }

        @AfterEach
        void tearDown() {
            driver.quit();
        }

        @Test
        @DisplayName("Should access power register page")
        void shouldAccessPowerRegisterPage() {

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));

            WebElement link = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Cadastrar")));
            link.click();

            String currentUrl = driver.getCurrentUrl();
            assertThat(currentUrl).contains("/cadastro");
        }

        @Nested
        class EditarPoderPageTest {

        }

        @Nested
        class DeletarPoderPageTest {

        }
    }
}