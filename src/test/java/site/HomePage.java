package site;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
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
    public String getHeaderText() {
        return driver.findElement(header).getText();
    }
    public String getCadastrarButtomText() {
        return driver.findElement(cadastrarButtom).getText();
    }

    public By getCadastrarButtom() {
        return cadastrarButtom;
    }
    public By getHeader() {
        return header;
    }
    public Point getHeaderLocation(){
        return driver.findElement(header).getLocation();
    }
    public Point getCadastrarButtomLocation(){
        return driver.findElement(cadastrarButtom).getLocation();
    }
    public Point getPowersListLocation(){
        return driver.findElement(powersList).getLocation();
    }
}
