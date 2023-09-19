package mk.ukim.finki.tech_prototype.cucumber.stepDefinitions;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class DeleteHotelStepDefinitions {
    private WebDriver driver;

    @Given("I am logged in as {string} with password {string} to delete a hotel")
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

    @And("I am on the home page")
    public void iAmOnTheHomePage() {
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("http://localhost:4200/home"));
    }

    @When("I navigate to the hotel list page")
    public void iNavigateToTheHotelListPage() {
        driver.get("http://localhost:4200/hotels");
    }

    @And("I search for the hotel with the name {string}")
    public void iSearchForTheHotelWithTheName(String hotelName) {
        WebElement searchInput = driver.findElement(By.name("filter"));
        searchInput.sendKeys(hotelName);
    }

    @And("I click on the delete button for that hotel")
    public void iClickOnTheDeleteButtonForThatHotel() {
        List<WebElement> searchResults = driver.findElements(By.className("card"));
        Assert.assertTrue(searchResults.size() > 0);

        WebElement deleteButton = driver.findElements(By.className("deleteHotelButton")).get(0);

        deleteButton.click();
    }

    @And("I search for the hotel with the name {string} again")
    public void iSearchForTheHotelWithTheNameAgain(String hotelName) {
        WebElement searchInput = driver.findElement(By.name("filter"));
        searchInput.sendKeys(hotelName);
    }

    @Then("the hotel should not be in the list")
    public void theHotelShouldNotBeInTheList() {
        List<WebElement> searchResults = driver.findElements(By.className("card"));
        Assert.assertEquals(0, searchResults.size());
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }


}
