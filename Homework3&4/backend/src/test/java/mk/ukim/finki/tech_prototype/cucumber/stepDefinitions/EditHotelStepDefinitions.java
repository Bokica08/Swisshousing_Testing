package mk.ukim.finki.tech_prototype.cucumber.stepDefinitions;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class EditHotelStepDefinitions {
    private ChromeDriver driver;

    @Given("I am logged in as {string} with password {string} to edit a hotel")
    public void iAmLoggedInAsWithPassword(String username, String password) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        this.driver = new ChromeDriver(options);
        driver.get("http://localhost:4200/login");

        WebElement usernameInput = driver.findElement(By.name("username"));
        WebElement passwordInput = driver.findElement(By.name("password"));
        WebElement loginButton = driver.findElement(By.id("loginButton"));

        usernameInput.sendKeys(username);
        passwordInput.sendKeys(password);
        loginButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS));

        wait.until(ExpectedConditions.textToBe(By.id("loggedInUser"), username));
    }

    @And("I am on the home page and redirected to list hotel page")
    public void iAmOnTheHomePage() {
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("http://localhost:4200/home"));
        driver.get("http://localhost:4200/hotels");
    }

    @And("I search for the hotel i want to edit with the name {string}")
    public void iSearchForTheHotelWithTheName(String hotelName) {
        WebElement searchInput = driver.findElement(By.name("filter"));
        searchInput.sendKeys(hotelName);
    }

    @And("I click on the edit button")
    public void iClickOnTheEditButton() {
        List<WebElement> searchResults = driver.findElements(By.className("card"));
        Assert.assertTrue(searchResults.size() > 0);

        WebElement editButton = driver.findElements(By.className("editHotelButton")).get(0);

        editButton.click();
    }

    @And("I fill in the hotel name with {string}")
    public void iFillInTheHotelNameWith(String hotelName) throws InterruptedException {
        WebElement hotelNameInput = driver.findElement(By.name("name"));
        hotelNameInput.clear();
        hotelNameInput.sendKeys(hotelName);

        driver.manage().window().maximize();

        ((JavascriptExecutor) driver).executeScript("scroll(0, 1000)"); // if the element is on bottom.

        Thread.sleep(2000);

        WebElement addButton = driver.findElement(By.id("editHotelButton"));
        addButton.click();
    }

    @Then("I am redirected to the list hotel page")
    public void iAmRedirectedToTheListHotelPage() throws InterruptedException {
        Thread.sleep(2000);
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("http://localhost:4200/hotels"));
    }

    @When("I search for the edited hotel name {string}")
    public void iSearchForTheEditedHotelName(String hotelName) {
        WebElement searchInput = driver.findElement(By.name("filter"));
        searchInput.sendKeys(hotelName);
    }

    @Then("I should see the hotel name {string} in the list")
    public void iShouldSeeTheHotelNameInTheList(String hotelName) {
        List<WebElement> searchResults = driver.findElements(By.className("card"));
        Assert.assertTrue(searchResults.size() > 0);

        WebElement element = searchResults.get(0);
        String pageSource = element.getAttribute("innerHTML");
        Assert.assertTrue(pageSource.contains(hotelName));
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

}
