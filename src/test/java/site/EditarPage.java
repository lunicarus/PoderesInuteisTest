package site;
import org.openqa.selenium.By;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;

public class EditarPage {
    protected WebDriver driver;
    private final By header =  By.xpath("//*[@id=\"root\"]/body/div/header/h1/a");
    private final By cadastrarButtom = By.xpath("//*[@id=\"root\"]/body/div/header/nav/ul/li/a");
    private final By powerForm = By.id("powerForm");
    private final By nomePoder = By.id("nome_do_poder");
    private final By descricao = By.id("descricao");
    private final By efeitosColaterais = By.id("efeitos_colaterais");
    private final By nota = By.id("nota");
    private final By submitButtom = By.xpath("//button[@type=\"submit\" and text()=\"Salvar Alterações\"]");

    public By getNomePoder() {
        return nomePoder;
    }

    public By getDescricao() {
        return descricao;
    }

    public By getEfeitosColaterais() {
        return efeitosColaterais;
    }

    public By getNota() {
        return nota;
    }

    public By getSubmitButtom() {
        return submitButtom;
    }

    public EditarPage(WebDriver driver){
        this.driver = driver;
        validateEditarPage(driver);
    }

    private void validateEditarPage(WebDriver driver) {
        this.driver = driver;
        driver.get("https://site-tc1.vercel.app/editar/0");
        if (!driver.getCurrentUrl().contains("https://site-tc1.vercel.app/editar/")) {
            throw new IllegalStateException("This is not the editar Page! \n" +
                    "The current page is: " + driver.getCurrentUrl());
        }
    }
    public Rectangle getCadastrarButtomLocation() {
        return driver.findElement(cadastrarButtom).getRect();
    }
    public Rectangle getHeaderLocation() {
        return driver.findElement(header).getRect();
    }
    public Rectangle getEfeitosColateraisLocation() {
        return driver.findElement(efeitosColaterais).getRect();
    }
    public Rectangle getNomePoderLocation() {
        return driver.findElement(nomePoder).getRect();
    }
    public Rectangle getDescricaoLocation() {
        return driver.findElement(descricao).getRect();
    }
    public  Rectangle getNotaLocation() {
        return driver.findElement(nota).getRect();
    }
    public Rectangle getSubmitButtomLocation() {
        return driver.findElement(submitButtom).getRect();
    }
}