package base;

import factory.DriverManager;
import io.cucumber.java.DataTableType;
import io.cucumber.java.DefaultParameterTransformer;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import managers.ConfigManager;
import org.openqa.selenium.By;
import pages.LoginPage;
import reports.AllureManager;
import utils.LogUtils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import static helpers.PropertiesHelper.loadAllFiles;

public abstract class BaseStep implements DefaultParameterTransformer {
    protected final Properties properties;

    // Constructor

    public BaseStep() {
        this.properties = loadAllFiles();
    }

    // Default Parameter Transformer

    public Object transform(String value, Type type) {
        if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            return Boolean.parseBoolean(value);
        }
        if (type.equals(Integer.class) || type.equals(int.class)) {
            return Integer.parseInt(value);
        }
        if (type.equals(Long.class) || type.equals(long.class)) {
            return Long.parseLong(value);
        }
        if (type.equals(Double.class) || type.equals(double.class)) {
            return Double.parseDouble(value);
        }
        return value;
    }

    // Common Navigation Steps

    @Given("I launch the browser")
    public void iLaunchTheBrowser() {
        logInfo("Browser launched - WebDriver should be initialized in CucumberHooks");
    }

    @When("I navigate to url {string}")
    public void iNavigateToUrl(String url) {
        getLoginPage().openURL(url);
    }

    @When("I navigate to the application")
    public void iNavigateToApplication() {
        String baseUrl = ConfigManager.getBaseUrl();
        getLoginPage().openURL(baseUrl);
    }

    @And("I wait for {int} second(s)")
    public void iWaitForSeconds(int seconds) {
        getLoginPage().sleep(seconds);
    }

    @And("I wait for page to load")
    public void iWaitForPageToLoad() {
        getLoginPage().waitForPageLoaded();
    }

    @And("I refresh the page")
    public void iRefreshThePage() {
        DriverManager.getDriver().navigate().refresh();
    }

    // Common Verification Steps

    @Then("I should see {string} text on the page")
    public void iShouldSeeTextOnThePage(String expectedText) {
        getLoginPage().verifyTextVisible(expectedText);
    }

    @Then("I should not see {string} text on the page")
    public void iShouldNotSeeTextOnThePage(String unexpectedText) {
        By locator = By.xpath("//*[contains(text(),'" + unexpectedText + "')]");
        getLoginPage().verifyTrue(!getLoginPage().isElementDisplayed(locator),
                "Text '" + unexpectedText + "' should not be visible on the page.");
    }

    @Then("I verify that {string} is visible successfully")
    public void iVerifyThatTextIsVisible(String expectedText) {
        getLoginPage().verifyTextVisible(expectedText);
    }

    @Then("I verify element {string} is visible")
    public void iVerifyElementIsVisible(String locatorKey) {
        By by = getByFromLocator(locatorKey);
        getLoginPage().verifyTrue(getLoginPage().isElementDisplayed(by),
                "Element '" + locatorKey + "' is not visible.");
    }

    @Then("I verify element {string} is displayed")
    public void iVerifyElementIsDisplayed(String locatorKey) {
        By by = getByFromLocator(locatorKey);
        getLoginPage().verifyTrue(getLoginPage().isElementDisplayed(by),
                "Element '" + locatorKey + "' is not displayed.");
    }

    @Then("I verify element {string} is not displayed")
    public void iVerifyElementIsNotDisplayed(String locatorKey) {
        By by = getByFromLocator(locatorKey);
        getLoginPage().verifyTrue(!getLoginPage().isElementDisplayed(by),
                "Element '" + locatorKey + "' should not be displayed.");
    }

    // Common URL Steps

    @Then("I should be on the {string} page")
    public void iShouldBeOnThePage(String pageName) {
        String currentUrl = getLoginPage().getCurrentUrl().toLowerCase(Locale.ROOT);
        String expectedUrlPart = pageName.toLowerCase(Locale.ROOT).replace(" ", "");

        getLoginPage().verifyTrue(
                currentUrl.contains(expectedUrlPart),
                "Expected URL to contain '" + expectedUrlPart + "' but was: " + currentUrl
        );
    }

    @Then("I should be redirected to {string}")
    public void iShouldBeRedirectedTo(String expectedUrl) {
        String currentUrl = getLoginPage().getCurrentUrl();
        getLoginPage().verifyTrue(
                currentUrl.contains(expectedUrl) || currentUrl.equals(expectedUrl),
                "Expected URL to contain or equal '" + expectedUrl + "' but was: " + currentUrl
        );
    }

    @Then("I verify current url is {string}")
    public void iVerifyCurrentUrlIs(String expectedUrl) {
        String currentUrl = getLoginPage().getCurrentUrl();
        getLoginPage().verifyEquals(currentUrl, expectedUrl);
    }

    @Then("I verify current url contains {string}")
    public void iVerifyCurrentUrlContains(String expectedPart) {
        String currentUrl = getLoginPage().getCurrentUrl();
        getLoginPage().verifyTrue(
                currentUrl.contains(expectedPart),
                "URL should contain '" + expectedPart + "' but was: " + currentUrl
        );
    }

    // Common Form Steps

    @When("I click element {string}")
    public void iClickElement(String locatorKey) {
        By by = getByFromLocator(locatorKey);
        getLoginPage().clickElement(by);
    }

    @When("I click element {string} with timeout {int}")
    public void iClickElementWithTimeout(String locatorKey, int timeout) {
        By by = getByFromLocator(locatorKey);
        getLoginPage().clickElement(by, timeout);
    }

    @When("I enter {string} in {string} field")
    public void iEnterTextInField(String text, String fieldName) {
        By by = getByFromLocator(fieldName);
        getLoginPage().setText(by, text);
    }

    @When("I clear {string} field")
    public void iClearField(String locatorKey) {
        By by = getByFromLocator(locatorKey);
        getLoginPage().getWebElement(by).clear();
    }

    @When("I press {string} key")
    public void iPressKey(String keyName) {
        switch (keyName.toLowerCase(Locale.ROOT)) {
            case "enter" -> getLoginPage().pressENTER();
            case "escape", "esc" -> getLoginPage().pressESC();
            case "f11" -> getLoginPage().pressF11();
            default -> throw new IllegalArgumentException("Unsupported key: " + keyName);
        }
    }

    //  Common Wait Steps

    @And("I wait for element {string} to be visible")
    public void iWaitForElementToBeVisible(String locatorKey) {
        By by = getByFromLocator(locatorKey);
        getLoginPage().waitForElementVisible(by);
    }

    @And("I wait for element {string} to be visible for {int} seconds")
    public void iWaitForElementToBeVisibleSeconds(String locatorKey, int seconds) {
        By by = getByFromLocator(locatorKey);
        getLoginPage().waitForElementVisible(by, seconds);
    }

    @And("I wait for element {string} to be clickable")
    public void iWaitForElementToBeClickable(String locatorKey) {
        By by = getByFromLocator(locatorKey);
        getLoginPage().waitForElementClickable(by);
    }

    @And("I wait for element {string} to be present")
    public void iWaitForElementToBePresent(String locatorKey) {
        By by = getByFromLocator(locatorKey);
        getLoginPage().waitForElementPresent(by);
    }

    @And("I wait for element {string} to be present for {int} seconds")
    public void iWaitForElementToBePresentSeconds(String locatorKey, int seconds) {
        By by = getByFromLocator(locatorKey);
        getLoginPage().waitForElementPresent(by, seconds);
    }

    // Common Scroll Steps

    @When("I scroll to element {string}")
    public void iScrollToElement(String locatorKey) {
        By by = getByFromLocator(locatorKey);
        getLoginPage().scrollToElement(by);
    }

    @When("I scroll to top of page")
    public void iScrollToTopOfPage() {
        getLoginPage().scrollToPosition(0, 0);
    }

    @When("I scroll to bottom of page")
    public void iScrollToBottomOfPage() {
        getLoginPage().scrollToPosition(0, 99999);
    }

    @When("I scroll to position {int},{int}")
    public void iScrollToPosition(int x, int y) {
        getLoginPage().scrollToPosition(x, y);
    }

    //  Common Mouse Actions

    @When("I hover over element {string}")
    public void iHoverOverElement(String locatorKey) {
        By by = getByFromLocator(locatorKey);
        getLoginPage().hoverElement(by);
    }

    @When("I move to element {string}")
    public void iMoveToElement(String locatorKey) {
        By by = getByFromLocator(locatorKey);
        getLoginPage().moveToElement(by);
    }

    //  Common Alert Steps

    @And("I accept alert")
    public void iAcceptAlert() {
        getLoginPage().acceptAlert();
    }

    //  Common Screenshot Steps

    @And("I take screenshot")
    public void iTakeScreenshot() {
        AllureManager.saveScreenshotPNG();
    }

    @And("I take screenshot with name {string}")
    public void iTakeScreenshotWithName(String name) {
        AllureManager.saveTextLog("Screenshot: " + name);
        AllureManager.saveScreenshotPNG();
    }

    //  Common Assertion Steps

    @Then("I verify {string} equals {string}")
    public void iVerifyEquals(String actual, String expected) {
        getLoginPage().verifyEquals(actual, expected);
    }

    @Then("I verify {string} equals {string} with message {string}")
    public void iVerifyEqualsWithMessage(String actual, String expected, String message) {
        getLoginPage().verifyEquals(actual, expected, message);
    }

    @Then("I verify condition is true")
    public void iVerifyConditionIsTrue(boolean condition) {
        getLoginPage().verifyTrue(condition);
    }

    @Then("I verify condition is true with message {string}")
    public void iVerifyConditionIsTrueWithMessage(boolean condition, String message) {
        getLoginPage().verifyTrue(condition, message);
    }

    @Then("I verify condition is false")
    public void iVerifyConditionIsFalse(boolean condition) {
        getLoginPage().verifyFalse(condition);
    }

    @Then("I verify condition is false with message {string}")
    public void iVerifyConditionIsFalseWithMessage(boolean condition, String message) {
        getLoginPage().verifyFalse(condition, message);
    }

    // Utility Methods (Protected for Subclasses)

    /**
     * Get LoginPage instance - override for custom page initialization
     */
    protected LoginPage getLoginPage() {
        return new LoginPage();
    }

    /**
     * Convert locator key to By object from properties
     * Format in properties: LOCATOR_KEY=type&&value (e.g., SUBMIT_BTN=xpath&&//button[@id='btn'])
     */
    protected By getByFromLocator(String locatorKey) {
        String locator = properties.getProperty(locatorKey);

        if (locator == null || locator.isEmpty()) {
            throw new IllegalArgumentException("Locator '" + locatorKey + "' not found in properties file.");
        }

        String[] parts = locator.split("&&");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid locator format for '" + locatorKey + "': " + locator);
        }

        String type = parts[0].trim().toLowerCase(Locale.ROOT);
        String value = parts[1].trim();

        return switch (type) {
            case "xpath" -> By.xpath(value);
            case "css", "cssselector" -> By.cssSelector(value);
            case "id" -> By.id(value);
            case "name" -> By.name(value);
            case "class", "classname" -> By.className(value);
            case "link", "linktext" -> By.linkText(value);
            case "partiallinktext", "partial" -> By.partialLinkText(value);
            case "tag", "tagname" -> By.tagName(value);
            default -> throw new IllegalArgumentException("Unsupported locator type: " + type);
        };
    }

    /**
     * Log info message
     */
    protected void logInfo(String message) {
        LogUtils.info(message);
    }

    /**
     * Log warning message
     */
    protected void logWarn(String message) {
        LogUtils.warn(message);
    }

    /**
     * Log error message
     */
    protected void logError(String message) {
        LogUtils.error(message);
    }

    // Data Table Helpers

    @DataTableType
    public Map<String, String> dataTableEntry(Map<String, String> entry) {
        return entry;
    }

    @DataTableType
    public List<String> stringList(List<String> entry) {
        return entry;
    }
}
