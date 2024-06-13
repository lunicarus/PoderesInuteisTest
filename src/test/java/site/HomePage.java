package site;
import org.openqa.selenium.By;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;
public class HomePage {
    protected WebDriver driver;

    private final By header =  By.xpath("//*[@id=\"root\"]/body/div/header/h1/a");
    private final By cadastrarButtom = By.xpath("//*[@id=\"root\"]/body/div/header/nav/ul/li/a");
    private final By powersList = By.id("powersList");
    public HomePage(WebDriver driver){
        this.driver = driver;
        driver.get("https://site-tc1.vercel.app/");
    }

    public By getCadastrarButtom() {
        return cadastrarButtom;
    }
    public Rectangle getHeaderLocation(){
        return driver.findElement(header).getRect();
    }
    public Rectangle getCadastrarButtomLocation(){
        return driver.findElement(cadastrarButtom).getRect();
    }
    public Rectangle getPowersListLocation(){
        return driver.findElement(powersList).getRect();
    }
}