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
    class HomePageTestUI{
        HomePage homePage;
        @BeforeEach
        void setUp(){
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
    class CadastrarPageTestUI{
        CadastrarPage cadastrarPage;

        @BeforeEach
        void setUp(){
            cadastrarPage  = new CadastrarPage(driver);
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

        @AfterEach
        void tearDown() {
            driver.quit();
        }

//        private void createNewPower() {
//
//        }

        @Nested
        class CreateRead {

            @Test
            @DisplayName("Should create a new power")
            void shouldCreateNewPower() {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));

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

        @Nested
        class Update {
            @Test
            @DisplayName("Should edit all fields of a power")
            void shouldEditAllFieldsOfPower() {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));

                WebElement link = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Cadastrar")));
                link.click();

                String nomeOriginal = faker.superhero().power();
                String descricaoOriginal = faker.lorem().sentence();
                String efeitosColateraisOriginal = faker.lorem().sentence();
                int notaOriginal = faker.number().numberBetween(1, 6);

                WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nome_do_poder")));
                WebElement descriptionInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("descricao")));
                WebElement efeitosColateraisInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("efeitos_colaterais")));
                WebElement notaSelect = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nota")));

                nameInput.sendKeys(nomeOriginal);
                descriptionInput.sendKeys(descricaoOriginal);
                efeitosColateraisInput.sendKeys(efeitosColateraisOriginal);
                new Select(notaSelect).selectByValue(String.valueOf(notaOriginal));

                WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit' and text()='Cadastrar Poder']")));
                submitButton.click();

                Alert alert = wait.until(ExpectedConditions.alertIsPresent());
                alert.accept();

                driver.get("https://site-tc1.vercel.app/");

                WebElement powerList = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("powersList")));
                List<WebElement> powers = powerList.findElements(By.className("post"));

                WebElement powerToEdit = powers.stream().filter(power -> {
                    String powerTitle = power.findElement(By.className("post-title")).getText();
                    return powerTitle.equals(nomeOriginal);
                }).findFirst().orElseThrow(() -> new AssertionError("Poder original não encontrado"));

                WebElement editLink = powerToEdit.findElement(By.linkText("Editar"));
                editLink.click();

                // Edição dos campos
                String nomeEditado = faker.superhero().power();
                String descricaoEditada = faker.lorem().sentence();
                String efeitosColateraisEditados = faker.lorem().sentence();
                int notaEditada = faker.number().numberBetween(1, 6);

                WebElement nameEditInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nome_do_poder")));
                WebElement descriptionEditInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("descricao")));
                WebElement efeitosColateraisEditInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("efeitos_colaterais")));
                WebElement notaEditSelect = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nota")));

                nameEditInput.clear();
                nameEditInput.sendKeys(nomeEditado);
                descriptionEditInput.clear();
                descriptionEditInput.sendKeys(descricaoEditada);
                efeitosColateraisEditInput.clear();
                efeitosColateraisEditInput.sendKeys(efeitosColateraisEditados);
                new Select(notaEditSelect).selectByValue(String.valueOf(notaEditada));

                WebElement submitEditButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit' and text()='Salvar Alterações']")));
                submitEditButton.click();

                Alert alertEdit = wait.until(ExpectedConditions.alertIsPresent());
                alertEdit.accept();

                driver.get("https://site-tc1.vercel.app/");

                WebElement powerListAfterEdit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("powersList")));
                List<WebElement> powersAfterEdit = powerListAfterEdit.findElements(By.className("post"));

                boolean powerEditedFound = powersAfterEdit.stream().anyMatch(power -> {
                    String powerTitle = power.findElement(By.className("post-title")).getText();
                    String powerDescription = power.findElement(By.className("post-excerpt")).getText();
                    String powerEfeitosColaterais = power.findElement(By.xpath(".//p[strong[text()='Efeitos Colaterais:']]")).getText();
                    String powerStars = power.findElement(By.className("stars")).getText();

                    return powerTitle.equals(nomeEditado) &&
                            powerDescription.equals(descricaoEditada) &&
                            powerEfeitosColaterais.contains(efeitosColateraisEditados) &&
                            powerStars.length() == notaEditada;
                });

                assertTrue(powerEditedFound, "O poder editado foi encontrado na lista.");
            }

            @Test
            @DisplayName("Should edit only the power name")
            void shouldEditPowerName() {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));

                WebElement link = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Cadastrar")));
                link.click();

                String nomeOriginal = faker.superhero().power();
                String descricaoOriginal = faker.lorem().sentence();
                String efeitosColateraisOriginal = faker.lorem().sentence();
                int notaOriginal = faker.number().numberBetween(1, 6);

                WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nome_do_poder")));
                WebElement descriptionInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("descricao")));
                WebElement efeitosColateraisInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("efeitos_colaterais")));
                WebElement notaSelect = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nota")));

                nameInput.sendKeys(nomeOriginal);
                descriptionInput.sendKeys(descricaoOriginal);
                efeitosColateraisInput.sendKeys(efeitosColateraisOriginal);
                new Select(notaSelect).selectByValue(String.valueOf(notaOriginal));

                WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit' and text()='Cadastrar Poder']")));
                submitButton.click();

                Alert alert = wait.until(ExpectedConditions.alertIsPresent());
                alert.accept();

                driver.get("https://site-tc1.vercel.app/");

                WebElement powerList = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("powersList")));
                List<WebElement> powers = powerList.findElements(By.className("post"));

                WebElement powerToEdit = powers.stream().filter(power -> {
                    String powerTitle = power.findElement(By.className("post-title")).getText();
                    return powerTitle.equals(nomeOriginal);
                }).findFirst().orElseThrow(() -> new AssertionError("Poder original não encontrado"));

                WebElement editLink = powerToEdit.findElement(By.linkText("Editar"));
                editLink.click();

                String nomeEditado = faker.superhero().power();

                WebElement nameEditInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nome_do_poder")));
                nameEditInput.clear();
                nameEditInput.sendKeys(nomeEditado);

                WebElement submitEditButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit' and text()='Salvar Alterações']")));
                submitEditButton.click();

                Alert alertEdit = wait.until(ExpectedConditions.alertIsPresent());
                alertEdit.accept();

                driver.get("https://site-tc1.vercel.app/");

                WebElement powerListAfterEdit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("powersList")));
                List<WebElement> powersAfterEdit = powerListAfterEdit.findElements(By.className("post"));

                boolean powerEditedFound = powersAfterEdit.stream().anyMatch(power -> {
                    String powerTitle = power.findElement(By.className("post-title")).getText();
                    return powerTitle.equals(nomeEditado);
                });

                assertTrue(powerEditedFound, "O poder editado foi encontrado na lista.");
            }

            @Test
            @DisplayName("Should edit the power note twice")
            void shouldEditPowerNoteTwice() {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));

                WebElement link = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Cadastrar")));
                link.click();

                String nomeOriginal = faker.superhero().power();
                String descricaoOriginal = faker.lorem().sentence();
                String efeitosColateraisOriginal = faker.lorem().sentence();
                int notaOriginal = faker.number().numberBetween(1, 6);

                WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nome_do_poder")));
                WebElement descriptionInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("descricao")));
                WebElement efeitosColateraisInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("efeitos_colaterais")));
                WebElement notaSelect = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nota")));

                nameInput.sendKeys(nomeOriginal);
                descriptionInput.sendKeys(descricaoOriginal);
                efeitosColateraisInput.sendKeys(efeitosColateraisOriginal);
                new Select(notaSelect).selectByValue(String.valueOf(notaOriginal));

                WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit' and text()='Cadastrar Poder']")));
                submitButton.click();

                Alert alert = wait.until(ExpectedConditions.alertIsPresent());
                alert.accept();

                driver.get("https://site-tc1.vercel.app/");

                WebElement powerList = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("powersList")));
                List<WebElement> powers = powerList.findElements(By.className("post"));

                WebElement powerToEdit = powers.stream().filter(power -> {
                    String powerTitle = power.findElement(By.className("post-title")).getText();
                    return powerTitle.equals(nomeOriginal);
                }).findFirst().orElseThrow(() -> new AssertionError("Poder original não encontrado"));

                WebElement editLink = powerToEdit.findElement(By.linkText("Editar"));
                editLink.click();

                // Primeira alteração da nota
                int notaEditada1 = faker.number().numberBetween(1, 6);
                while (notaEditada1 == notaOriginal) {
                    notaEditada1 = faker.number().numberBetween(1, 6);
                }

                WebElement notaEditSelect1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nota")));
                new Select(notaEditSelect1).selectByValue(String.valueOf(notaEditada1));

                WebElement submitEditButton1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit' and text()='Salvar Alterações']")));
                submitEditButton1.click();

                Alert alertEdit1 = wait.until(ExpectedConditions.alertIsPresent());
                alertEdit1.accept();

                driver.get("https://site-tc1.vercel.app/");

                // Segunda alteração da nota
                powerList = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("powersList")));
                powers = powerList.findElements(By.className("post"));

                powerToEdit = powers.stream().filter(power -> {
                    String powerTitle = power.findElement(By.className("post-title")).getText();
                    return powerTitle.equals(nomeOriginal);
                }).findFirst().orElseThrow(() -> new AssertionError("Poder original não encontrado"));

                editLink = powerToEdit.findElement(By.linkText("Editar"));
                editLink.click();

                int notaEditada2 = faker.number().numberBetween(1, 6);
                while (notaEditada2 == notaEditada1) {
                    notaEditada2 = faker.number().numberBetween(1, 6);
                }

                WebElement notaEditSelect2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nota")));
                new Select(notaEditSelect2).selectByValue(String.valueOf(notaEditada2));

                WebElement submitEditButton2 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit' and text()='Salvar Alterações']")));
                submitEditButton2.click();

                Alert alertEdit2 = wait.until(ExpectedConditions.alertIsPresent());
                alertEdit2.accept();

                driver.get("https://site-tc1.vercel.app/");

                WebElement powerListAfterEdit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("powersList")));
                List<WebElement> powersAfterEdit = powerListAfterEdit.findElements(By.className("post"));

                int finalNotaEditada = notaEditada2;
                boolean powerEditedTwiceFound = powersAfterEdit.stream().anyMatch(power -> {
                    String powerTitle = power.findElement(By.className("post-title")).getText();
                    String powerDescription = power.findElement(By.className("post-excerpt")).getText();
                    String powerEfeitosColaterais = power.findElement(By.xpath(".//p[strong[text()='Efeitos Colaterais:']]")).getText();
                    String powerStars = power.findElement(By.className("stars")).getText();

                    return powerTitle.equals(nomeOriginal) &&
                            powerDescription.equals(descricaoOriginal) &&
                            powerEfeitosColaterais.contains(efeitosColateraisOriginal) &&
                            powerStars.length() == finalNotaEditada;
                });

                assertTrue(powerEditedTwiceFound, "O poder foi encontrado na lista com a nota editada duas vezes");
            }

            @Nested
            class Delete {

                @Test
                @DisplayName("Should delete a power")
                void shouldDeletePower() {
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));

                    WebElement link = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Cadastrar")));
                    link.click();

                    String nomeOriginal = faker.superhero().power();
                    String descricaoOriginal = faker.lorem().sentence();
                    String efeitosColateraisOriginal = faker.lorem().sentence();
                    int notaOriginal = faker.number().numberBetween(1, 6);

                    WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nome_do_poder")));
                    WebElement descriptionInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("descricao")));
                    WebElement efeitosColateraisInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("efeitos_colaterais")));
                    WebElement notaSelect = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nota")));

                    nameInput.sendKeys(nomeOriginal);
                    descriptionInput.sendKeys(descricaoOriginal);
                    efeitosColateraisInput.sendKeys(efeitosColateraisOriginal);
                    new Select(notaSelect).selectByValue(String.valueOf(notaOriginal));

                    WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit' and text()='Cadastrar Poder']")));
                    submitButton.click();

                    Alert alert = wait.until(ExpectedConditions.alertIsPresent());
                    alert.accept();

                    driver.get("https://site-tc1.vercel.app/");

                    WebElement powerList = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("powersList")));
                    List<WebElement> powers = powerList.findElements(By.className("post"));

                    WebElement powerToDelete = powers.stream().filter(power -> {
                        String powerTitle = power.findElement(By.className("post-title")).getText();
                        return powerTitle.equals(nomeOriginal);
                    }).findFirst().orElseThrow(() -> new AssertionError("Poder original não encontrado"));

                    WebElement deleteButton = powerToDelete.findElement(By.xpath(".//div[@class='post-actions']/button[@data-action='delete']"));
                    deleteButton.click();

                    Alert deleteAlert = wait.until(ExpectedConditions.alertIsPresent());
                    deleteAlert.accept();

                    assertNotNull(deleteAlert, "O alerta de confirmação de exclusão foi aceito com sucesso.");

                }
            }
        }
    }
}

