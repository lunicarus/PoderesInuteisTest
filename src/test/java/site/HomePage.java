package site;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
public class HomePage {
    protected WebDriver driver;

    private final By header =  By.xpath("//*[@id=\"root\"]/body/div/header/h1/a");
    private final By cadastrarButtom = By.xpath("//*[@id=\"root\"]/body/div/header/nav/ul/li/a");

    public HomePage(WebDriver driver){
        this.driver = driver;
        driver.get("https://site-tc1.vercel.app/");
    }
    public String getHeaderText() {
        return driver.findElement(header).getText();
    }
    public String getCadastrarButtomText() {
        return driver.findElement(cadastrarButtom).getText();
    }

    public By getCadastrarButtom() {
        return cadastrarButtom;
    }
}
