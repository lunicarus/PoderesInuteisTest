package site;

import com.github.javafaker.Faker;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
//import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        @Test
        @DisplayName("Should create a new power")
        void shouldCreateNewPower() {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement link = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Cadastrar")));
            link.click();

            String nome = faker.superhero().power();
            String descricao = faker.lorem().sentence();
            String efeitosColaterais = faker.lorem().sentence();
            int nota = faker.number().numberBetween(1, 6); // entre 1 e 5

            WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nome_do_poder")));
            WebElement descriptionInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("descricao")));
            WebElement efeitosColateraisInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("efeitos_colaterais")));
            WebElement notaSelect = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nota")));

            nameInput.sendKeys(nome);
            descriptionInput.sendKeys(descricao);
            efeitosColateraisInput.sendKeys(efeitosColaterais);
            new Select(notaSelect).selectByValue(String.valueOf(nota));

            WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit' and text()='Cadastrar Poder']")));
            submitButton.click();

            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String alertText = alert.getText();
            assertTrue(alertText.contains("Poder criado com sucesso!"));
            alert.accept();

            driver.get("https://site-tc1.vercel.app/");

            WebElement powerList = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("powersList"))); // Substitua pelo ID da lista de poderes
            List<WebElement> powers = powerList.findElements(By.className("post")); // Supondo que cada poder é um <div> com a classe 'post'

            boolean powerFound = powers.stream().anyMatch(power -> {
                String powerTitle = power.findElement(By.className("post-title")).getText();
                String powerDescription = power.findElement(By.className("post-excerpt")).getText();
                String powerEfeitosColaterais = power.findElement(By.xpath(".//p[strong[text()='Efeitos Colaterais:']]")).getText();
                String powerStars = power.findElement(By.className("stars")).getText();

                boolean titleMatches = powerTitle.equals(nome);
                boolean descriptionMatches = powerDescription.equals(descricao);
                boolean efeitosColateraisMatch = powerEfeitosColaterais.contains(efeitosColaterais);
                boolean starsMatch = powerStars.length() == nota;

                if (titleMatches && descriptionMatches && efeitosColateraisMatch && starsMatch) {
                    WebElement editButton = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Editar")));
                    WebElement deleteButton = power.findElement(By.xpath(".//div[@class='post-actions']/button[@data-action='delete']"));
                    return editButton != null && deleteButton != null;
                }
                return false;
            });

            assertTrue(powerFound, "O novo poder foi encontrado na lista com os botões Editar e Excluir.");
        }


    }
}