package site;

import com.github.javafaker.Faker;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
//import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    private WebDriver driver;

    @BeforeEach
    void setUp() {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Nested
    class HomePageTestUI {
        HomePage homePage;

        @BeforeEach
        void setUp() {
            homePage = new HomePage(driver);
        }

        @Test
        @DisplayName("Should access power register page")
        void shouldAccessPowerRegisterPage() {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement link = wait.until(ExpectedConditions.elementToBeClickable(homePage.getCadastrarButtom()));
            link.click();

            String currentUrl = driver.getCurrentUrl();
            assertThat(currentUrl).contains("/cadastro");

        }

        @Test
        @DisplayName("test if components of page overlap")
        void componentsShouldNotOverlap() {
            Dimension[] sizes = {
                    new Dimension(320, 480),
                    new Dimension(480, 320),
                    new Dimension(768, 1024),
                    new Dimension(1024, 768),
                    new Dimension(1366, 768),
                    new Dimension(1920, 1080)
            };
            for (Dimension size : sizes) {
                driver.manage().window().setSize(size);
                driver.manage().timeouts().implicitlyWait(Duration.ofMillis(1000));

                assertNotSame(homePage.getHeaderLocation(), homePage.getCadastrarButtomLocation());

                assertNotSame(homePage.getPowersListLocation(), homePage.getCadastrarButtomLocation());

                assertNotSame(homePage.getHeaderLocation(), homePage.getPowersListLocation());
            }

        }
    }

    @Nested
    class CadastrarPageTestUI {
        CadastrarPage cadastrarPage;

        @BeforeEach
        void setUp() {
            cadastrarPage = new CadastrarPage(driver);
        }

    }

    @Nested
    class CRUDtests {
        private Faker faker;

        @BeforeEach
        void setUp() {
            driver.get("https://site-tc1.vercel.app/");
            faker = new Faker();
        }

        void cadastrarPoder(String nome, String descricao, String efeitosColaterais, int nota) {
            CadastrarPage cadastrarPage = new CadastrarPage(driver);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));

            WebElement link = wait.until(ExpectedConditions.elementToBeClickable(cadastrarPage.getSubmitButtom()));
            link.click();

            WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(cadastrarPage.getNomePoder()));
            WebElement descriptionInput = wait.until(ExpectedConditions.visibilityOfElementLocated(cadastrarPage.getDescricao()));
            WebElement efeitosColateraisInput = wait.until(ExpectedConditions.visibilityOfElementLocated(cadastrarPage.getEfeitosColaterais()));
            WebElement notaSelect = wait.until(ExpectedConditions.visibilityOfElementLocated(cadastrarPage.getNota()));

            nameInput.sendKeys(nome);
            descriptionInput.sendKeys(descricao);
            efeitosColateraisInput.sendKeys(efeitosColaterais);
            new Select(notaSelect).selectByValue(String.valueOf(nota));

            WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(cadastrarPage.getSubmitButtom()));
            submitButton.click();

            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            alert.accept();
        }

        WebElement encontrarPoder(String nomeOriginal) {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
            driver.get("https://site-tc1.vercel.app/");
            WebElement powerList = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("powersList")));
            List<WebElement> powers = powerList.findElements(By.className("post"));

            return powers.stream().filter(power -> {
                String powerTitle = power.findElement(By.className("post-title")).getText();
                return powerTitle.equals(nomeOriginal);
            }).findFirst().orElseThrow(() -> new AssertionError("Poder não encontrado"));
        }

        void editarPoder(WebElement power, String nome, String descricao, String efeitosColaterais, int nota) {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
            WebElement editLink = power.findElement(By.linkText("Editar"));
            editLink.click();

            WebElement nameEditInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nome_do_poder")));
            WebElement descriptionEditInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("descricao")));
            WebElement efeitosColateraisEditInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("efeitos_colaterais")));
            WebElement notaEditSelect = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nota")));

            nameEditInput.clear();
            nameEditInput.sendKeys(nome);
            descriptionEditInput.clear();
            descriptionEditInput.sendKeys(descricao);
            efeitosColateraisEditInput.clear();
            efeitosColateraisEditInput.sendKeys(efeitosColaterais);
            new Select(notaEditSelect).selectByValue(String.valueOf(nota));

            WebElement submitEditButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit' and text()='Salvar Alterações']")));
            submitEditButton.click();

            Alert alertEdit = wait.until(ExpectedConditions.alertIsPresent());
            alertEdit.accept();
        }

        @Nested
        class CreateRead {
            CadastrarPage cadastrarPage;

            @BeforeEach
            void setUp() {
                cadastrarPage = new CadastrarPage(driver);
            }

            @Test
            @DisplayName("Should create a new power")
            void shouldCreateNewPower() {
                String nome = faker.superhero().power();
                String descricao = faker.lorem().sentence();
                String efeitosColaterais = faker.lorem().sentence();
                int nota = faker.number().numberBetween(1, 6); // entre 1 e 5

                cadastrarPoder(nome, descricao, efeitosColaterais, nota);

                WebElement powerToVerify = encontrarPoder(nome);

                String powerTitle = powerToVerify.findElement(By.className("post-title")).getText();
                String powerDescription = powerToVerify.findElement(By.className("post-excerpt")).getText();
                String powerEfeitosColaterais = powerToVerify.findElement(By.xpath(".//p[strong[text()='Efeitos Colaterais:']]")).getText();
                String powerStars = powerToVerify.findElement(By.className("stars")).getText();

                assertTrue(powerTitle.equals(nome));
                assertTrue(powerDescription.equals(descricao));
                assertTrue(powerEfeitosColaterais.contains(efeitosColaterais));
                assertTrue(powerStars.length() == nota);
            }

//            @Test
//            @DisplayName("Should not allow adding two powers with the same name")
//            void shouldNotAllowAddingTwoPowersWithSameName() {
//                String nome = faker.superhero().power();
//                String descricao1 = faker.lorem().sentence();
//                String descricao2 = faker.lorem().sentence();
//                String efeitosColaterais1 = faker.lorem().sentence();
//                String efeitosColaterais2 = faker.lorem().sentence();
//                int nota1 = faker.number().numberBetween(1, 6); // entre 1 e 5
//                int nota2 = faker.number().numberBetween(1, 6); // entre 1 e 5
//
//                cadastrarPoder(nome, descricao1, efeitosColaterais1, nota1);
//
//                cadastrarPoder(nome, descricao2, efeitosColaterais2, nota2);
//
//                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
//                try {
//                    Alert alert = wait.until(ExpectedConditions.alertIsPresent());
//                    String alertText = alert.getText();
//                    alert.accept();
//                    assertTrue(alertText.contains("existe"), "Não ha uma mensagem de erro informando que o poder já existe");
//                } catch (TimeoutException e) {
//                    List<WebElement> powers = driver.findElements(By.className("post"));
//                    long count = powers.stream()
//                            .filter(power -> power.findElement(By.className("post-title")).getText().equals(nome))
//                            .count();
//                    assertEquals(1, count, "Não podem existir dois poderes com o mesmo nome");
//                }
//            }

        }

        @Nested
        class Update {
            @Test
            @DisplayName("Should edit all fields of a power")
            void shouldEditAllFieldsOfPower() {
                String nomeOriginal = faker.superhero().power();
                String descricaoOriginal = faker.lorem().sentence();
                String efeitosColateraisOriginal = faker.lorem().sentence();
                int notaOriginal = faker.number().numberBetween(1, 6);

                cadastrarPoder(nomeOriginal, descricaoOriginal, efeitosColateraisOriginal, notaOriginal);

                WebElement powerToEdit = encontrarPoder(nomeOriginal);

                String nomeEditado = faker.superhero().power();
                String descricaoEditada = faker.lorem().sentence();
                String efeitosColateraisEditados = faker.lorem().sentence();
                int notaEditada = faker.number().numberBetween(1, 6);

                editarPoder(powerToEdit, nomeEditado, descricaoEditada, efeitosColateraisEditados, notaEditada);

                WebElement powerToVerify = encontrarPoder(nomeEditado);

                String powerTitle = powerToVerify.findElement(By.className("post-title")).getText();
                String powerDescription = powerToVerify.findElement(By.className("post-excerpt")).getText();
                String powerEfeitosColaterais = powerToVerify.findElement(By.xpath(".//p[strong[text()='Efeitos Colaterais:']]")).getText();
                String powerStars = powerToVerify.findElement(By.className("stars")).getText();

                assertTrue(powerTitle.equals(nomeEditado));
                assertTrue(powerDescription.equals(descricaoEditada));
                assertTrue(powerEfeitosColaterais.contains(efeitosColateraisEditados));
                assertTrue(powerStars.length() == notaEditada);
            }

            @Test
            @DisplayName("Should edit only the power name")
            void shouldEditPowerName() {
                String nomeOriginal = faker.superhero().power();
                String descricaoOriginal = faker.lorem().sentence();
                String efeitosColateraisOriginal = faker.lorem().sentence();
                int notaOriginal = faker.number().numberBetween(1, 6);

                cadastrarPoder(nomeOriginal, descricaoOriginal, efeitosColateraisOriginal, notaOriginal);

                WebElement powerToEdit = encontrarPoder(nomeOriginal);

                String nomeEditado = faker.superhero().power();

                editarPoder(powerToEdit, nomeEditado, descricaoOriginal, efeitosColateraisOriginal, notaOriginal);

                WebElement powerToVerify = encontrarPoder(nomeEditado);

                String powerTitle = powerToVerify.findElement(By.className("post-title")).getText();

                assertTrue(powerTitle.equals(nomeEditado));
            }

//            @Test
//            @DisplayName("Should not allow editing power name to an existing one")
//            void shouldNotAllowEditingPowerNameToExisting() {
//                String nomeOriginal = faker.superhero().power();
//                String descricaoOriginal = faker.lorem().sentence();
//                String efeitosColateraisOriginal = faker.lorem().sentence();
//                int notaOriginal = faker.number().numberBetween(1, 6);
//
//                cadastrarPoder(nomeOriginal, descricaoOriginal, efeitosColateraisOriginal, notaOriginal);
//
//                WebElement powerToEdit = encontrarPoder(nomeOriginal);
//
//
//                String nomeExistente = faker.superhero().power();
//                String descricaoExistente = faker.lorem().sentence();
//                String efeitosColateraisExistente = faker.lorem().sentence();
//                int notaExistente = faker.number().numberBetween(1, 6);
//
//                cadastrarPoder(nomeExistente, descricaoExistente, efeitosColateraisExistente, notaExistente);
//
//                editarPoder(powerToEdit, nomeExistente, descricaoOriginal, efeitosColateraisOriginal, notaOriginal);
//
//                WebElement powerToVerify = encontrarPoder(nomeOriginal);
//                String powerTitle = powerToVerify.findElement(By.className("post-title")).getText();
//                assertTrue(powerTitle.equals(nomeOriginal), "O nome do poder original não pode ser alterado para um nome existente");
//            }

            @Test
            @DisplayName("Should edit the power note twice")
            void shouldEditPowerNoteTwice() {
                String nomeOriginal = faker.superhero().power();
                String descricaoOriginal = faker.lorem().sentence();
                String efeitosColateraisOriginal = faker.lorem().sentence();
                int notaOriginal = faker.number().numberBetween(1, 6);

                cadastrarPoder(nomeOriginal, descricaoOriginal, efeitosColateraisOriginal, notaOriginal);

                WebElement powerToEdit = encontrarPoder(nomeOriginal);

                int notaEditada1 = faker.number().numberBetween(1, 6);
                while (notaEditada1 == notaOriginal) {
                    notaEditada1 = faker.number().numberBetween(1, 6);
                }

                editarPoder(powerToEdit, nomeOriginal, descricaoOriginal, efeitosColateraisOriginal, notaEditada1);
                powerToEdit = encontrarPoder(nomeOriginal);

                int notaEditada2 = faker.number().numberBetween(1, 6);
                while (notaEditada2 == notaEditada1) {
                    notaEditada2 = faker.number().numberBetween(1, 6);
                }

                editarPoder(powerToEdit, nomeOriginal, descricaoOriginal, efeitosColateraisOriginal, notaEditada2);

                WebElement powerToVerify = encontrarPoder(nomeOriginal);

                String powerTitle = powerToVerify.findElement(By.className("post-title")).getText();
                String powerDescription = powerToVerify.findElement(By.className("post-excerpt")).getText();
                String powerEfeitosColaterais = powerToVerify.findElement(By.xpath(".//p[strong[text()='Efeitos Colaterais:']]")).getText();
                String powerStars = powerToVerify.findElement(By.className("stars")).getText();

                assertTrue(powerTitle.equals(nomeOriginal));
                assertTrue(powerDescription.equals(descricaoOriginal));
                assertTrue(powerEfeitosColaterais.contains(efeitosColateraisOriginal));
                assertTrue(powerStars.length() == notaEditada2);
            }

        }

            @Nested
            class Delete {
                @Test
                @DisplayName("Should delete a power")
                void shouldDeletePower() {
                    String nomeOriginal = faker.superhero().power();
                    String descricaoOriginal = faker.lorem().sentence();
                    String efeitosColateraisOriginal = faker.lorem().sentence();
                    int notaOriginal = faker.number().numberBetween(1, 6);

                    cadastrarPoder(nomeOriginal, descricaoOriginal, efeitosColateraisOriginal, notaOriginal);

                    WebElement powerToDelete = encontrarPoder(nomeOriginal);

                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
                    WebElement deleteButton = powerToDelete.findElement(By.xpath(".//div[@class='post-actions']/button[@data-action='delete']"));
                    deleteButton.click();

                    Alert alert = wait.until(ExpectedConditions.alertIsPresent());
                    alert.accept();

                    driver.navigate().refresh();

                    assertThrows(AssertionError.class, () -> {
                        try {
                            encontrarPoder(nomeOriginal);
                        } catch (TimeoutException e) {
                            throw new AssertionError("O poder foi excluído");
                        }
                    });
                }
            }
        }
    }



