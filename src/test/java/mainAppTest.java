import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class mainAppTest {
    WebDriver driver;
    String baseUrl;
    Wait<WebDriver> wait;
    JavascriptExecutor executor;
    Actions builder;
    @Before
    public void beforeTest(){
        System.setProperty("webdriver.chrome.driver", "src/drivers/chromedriver.exe");
        driver = new ChromeDriver();
        baseUrl = "https://www.sberbank.ru";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(30,TimeUnit.SECONDS);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, 10, 1000);
        executor = (JavascriptExecutor)driver;
        builder = new Actions(driver);
    }
    @Test
    public void mainTest(){
        driver.get(baseUrl);
        goToAndWait("//span[contains(text(), '»потека')]", "//a[contains(text(), '»потека на готовое жиль')]");
        changeFrame("iFrameResizer0");
        fullFill("//*[@id=\"estateCost\"]", "5180000");
        waitUntilItChanges("4 403 000 \u20BD","46 244 \u20BD", "66 062 \u20BD", "9,6 %");
        fullFill("//input[contains(@id, 'initialFee')]","3058000");
        waitUntilItChanges("2 122 000 \u20BD","21 776 \u20BD", "36 294 \u20BD", "9,2 %");
        fullFill("//input[contains(@id, 'creditTerm')]", "30");
        waitUntilItChanges("2 122 000 \u20BD","17 381 \u20BD", "28 968 \u20BD", "9,2 %");
        click("//input[contains(@data-test-id, 'paidToCard')]/parent::label/parent::div");
        waitUntilItChanges("2 122 000 \u20BD","18 154 \u20BD", "30 256 \u20BD", "9,7 %");
        clickWaitAndAssert("//input[contains(@data-test-id, 'canConfirmIncome')]/parent::label/parent::div");
        waitUntilItChanges("2 122 000 \u20BD","18 623 \u20BD", "31 037 \u20BD", "10,0 %");
        //builder.moveToElement(driver.findElement(By.xpath("//input[contains(@data-test-id, 'youngFamilyDiscount')]"))).perform();
        //((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//input[contains(@data-test-id, 'youngFamilyDiscount')]/parent::label/parent::div")));
        click("//*[@id=\"calc-main\"]/div/div/div[2]/div[5]/div[1]/div[2]/label/span[3]/svg");
        //executor.executeScript("arguments[0].click();", driver.findElement(By.xpath("//input[contains(@data-test-id, 'youngFamilyDiscount')]/parent::label/parent::div")));
        waitUntilItChanges("2 122 000 \u20BD","17 998 \u20BD", "29 997 \u20BD", "9,6 %");
    }
    @After
    public void afterTest(){
        driver.quit();
    }

    public void click(String xpath){
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(xpath))));
        builder.click(driver.findElement(By.xpath(xpath))).perform();
    }

    public void changeFrame(String frameID){
        driver.switchTo().frame(driver.findElement(By.id(frameID)));
    }

    public void goToAndWait(String xpath, String clickAfterWait){
        builder.moveToElement(driver.findElement(By.xpath(xpath))).perform();
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(clickAfterWait)))).click();
    }
    public void fullFill(String xpath, String yourKeys){
        driver.findElement(By.xpath(xpath)).clear();
        driver.findElement(By.xpath(xpath)).sendKeys(yourKeys);
    }
    public void clickWaitAndAssert(String clickAfterWait){
        Assert.assertTrue(driver.findElement(By.xpath(clickAfterWait)).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath(clickAfterWait)).isEnabled());
        executor.executeScript("arguments[0].click();", driver.findElement(By.xpath(clickAfterWait)));
        click(clickAfterWait);
    }
    public void waitUntilItChanges(String amountOfCredit, String mounthlyPayment, String requiredIncome, String rate){
        wait.until(ExpectedConditions.textToBe(By.xpath("//span[contains(@data-test-id, 'amountOfCredit')]"), amountOfCredit));
        wait.until(ExpectedConditions.textToBe(By.xpath("//span[contains(@data-test-id, 'monthlyPayment')]"), mounthlyPayment));
        wait.until(ExpectedConditions.textToBe(By.xpath("//span[contains(@data-test-id, 'requiredIncome')]"), requiredIncome));
        wait.until(ExpectedConditions.textToBe(By.xpath("//span[contains(@data-test-id, 'rate')]"), rate));
    }
}
