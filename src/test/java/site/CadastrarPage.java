package site;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CadastrarPage {
    protected WebDriver driver;
    private final By header =  By.xpath("//*[@id=\"root\"]/body/div/header/h1/a");
    private final By cadastrarButtom = By.xpath("//*[@id=\"root\"]/body/div/header/nav/ul/li/a");
    private final By powerForm = By.id("powerForm");
    private final By nomePoder = By.id("nome_do_poder");
    private final By descricao = By.id("descricao");
    private final By efeitosColaterais = By.id("efeitos_colaterais");
    private final By nota = By.id("nota");

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

    public CadastrarPage(WebDriver driver){
        this.driver = driver;
        validateCadastrarPage(driver);
    }

    private void validateCadastrarPage(WebDriver driver) {
        this.driver = driver;
        driver.get("https://site-tc1.vercel.app/cadastro");
        if (!driver.getCurrentUrl().equals("https://site-tc1.vercel.app/cadastro")) {
            throw new IllegalStateException("This is not the Cadastrar Page! \n" +
                    "The current page is: " + driver.getCurrentUrl());
        }
    }

}
