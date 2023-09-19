package mk.ukim.finki.tech_prototype.cucumber.stepDefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

public class AddHotelStepDefinitions {
    private WebDriver driver;

    @Given("I am logged in as {string} with password {string}")
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

    @And("I am on the home page and redirected to add hotel page")
    public void iAmOnTheHomePage() {
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("http://localhost:4200/home"));
        driver.get("http://localhost:4200/add-hotel");
    }

    @When("I add a hotel with the following details:")
    public void iAddAHotelWithTheFollowingDetails(DataTable dataTable) throws InterruptedException {

        List<Map<String, String>> hotelData = dataTable.asMaps(String.class, String.class);

        WebElement xInput = driver.findElement(By.name("x"));
        WebElement yInput = driver.findElement(By.name("y"));
        WebElement nameInput = driver.findElement(By.name("name"));
        WebElement cityInput = driver.findElement(By.name("city"));
        WebElement streetInput = driver.findElement(By.name("street"));
        WebElement houseNumberInput = driver.findElement(By.name("houseNumber"));
        WebElement descriptionInput = driver.findElement(By.name("description"));
        WebElement imagePathInput = driver.findElement(By.name("imagePath"));
        WebElement websiteInput = driver.findElement(By.name("website"));
        WebElement phoneNumberInput = driver.findElement(By.name("phone"));
        WebElement starsInput = driver.findElement(By.name("stars"));

        xInput.sendKeys(hotelData.get(0).get("X"));
        yInput.sendKeys(hotelData.get(0).get("Y"));
        nameInput.sendKeys(hotelData.get(0).get("Name"));
        cityInput.sendKeys(hotelData.get(0).get("City"));
        streetInput.sendKeys(hotelData.get(0).get("Street"));
        houseNumberInput.sendKeys(hotelData.get(0).get("House Number"));
        descriptionInput.sendKeys(hotelData.get(0).get("Description"));
        imagePathInput.sendKeys(hotelData.get(0).get("Image Path"));
        websiteInput.sendKeys(hotelData.get(0).get("Website"));
        phoneNumberInput.sendKeys(hotelData.get(0).get("Phone Number"));
        starsInput.sendKeys(hotelData.get(0).get("Stars"));

        driver.manage().window().maximize();

        ((JavascriptExecutor) driver).executeScript("scroll(0, 1000)"); // if the element is on bottom.

        Thread.sleep(2000);

        WebElement addButton = driver.findElement(By.id("addHotelButton"));
        addButton.click();
    }

    @Then("I should be redirected to the hotels page")
    public void iShouldBeRedirectedToTheHotelsPage() throws InterruptedException {
        Thread.sleep(2000);
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("http://localhost:4200/hotels"));
    }

    @When("I select {string} as the search attribute")
    public void iSelectAsTheSearchAttribute(String searchAttribute) {
        WebElement searchAttributeDropdown = driver.findElement(By.name("hotels"));

        Select select = new Select(searchAttributeDropdown);

        select.selectByVisibleText(searchAttribute);
    }

    @And("I enter {string} in the search field")
    public void iEnterInTheSearchField(String hotelName) {
        WebElement searchInput = driver.findElement(By.name("filter"));
        searchInput.sendKeys(hotelName);
    }

    @Then("I should see {string} in the search results")
    public void iShouldSeeInTheSearchResults(String hotelName) {
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

