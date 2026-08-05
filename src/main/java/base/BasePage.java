package base;

import factory.DriverManager;
import helpers.CaptureHelper;
import helpers.SystemHelper;
import managers.ConfigManager;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import reports.AllureManager;
import strategies.ElementResolver;
import utils.LogUtils;
import utils.ObjectUtils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

/**
 * Abstract generic base page wrapping all WebUI operations.
 * All page objects extend this class with fluent pattern: class LoginPage extends BasePage&lt;LoginPage&gt;
 */
public abstract class BasePage<T extends BasePage<T>> {

    // Protected Config Getters

    protected static long getExplicitTimeout() {
        return ConfigManager.getExplicitWaitTimeout();
    }

    protected static long getStepTime() {
        return ConfigManager.getStepTime();
    }

    protected static long getPageLoadTimeout() {
        return ConfigManager.getPageLoadTimeout();
    }

    // Public URL / Window operations

    public String getCurrentUrl() {
        String url = DriverManager.getDriver().getCurrentUrl();
        LogUtils.info("Get current URL: " + url);
        return url;
    }

    public void maximizeWindow() {
        DriverManager.getDriver().manage().window().maximize();
        LogUtils.info("Window maximized");
    }

    public void openURL(String url) {
        DriverManager.getDriver().get(url);
        sleep(getStepTime());
        LogUtils.info("Open URL: " + url);
        AllureManager.saveTextLog("Open URL: " + url);
        waitForPageLoaded();
        if (ConfigManager.isScreenshotStepAllEnabled()) {
            CaptureHelper.takeScreenshot("openURL_" + SystemHelper.makeSlug(url));
        }
    }

    // Public Element Finding

    public WebElement getWebElement(By by) {
        return ElementResolver.getInstance().findElement(by);
    }

    public List<WebElement> getWebElements(By by) {
        return ElementResolver.getInstance().findElements(by);
    }

    public String getElementText(By by) {
        waitForPageLoaded();
        waitForElementVisible(by);
        sleep(getStepTime());
        String text = getWebElement(by).getText();
        LogUtils.info("Get text: " + text);
        return text;
    }

    // Public Click Operations

    public void clickElement(By by) {
        waitForPageLoaded();
        waitForElementVisible(by);
        sleep(getStepTime());
        try {
            getWebElement(by).click();
        } catch (ElementClickInterceptedException e) {
            LogUtils.warn("Click intercepted for " + by + ", fallback to JS click");
            ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].click();", getWebElement(by));
        }
        LogUtils.info("Click element " + by);
        if (ConfigManager.isScreenshotStepAllEnabled()) {
            CaptureHelper.takeScreenshot("clickElement_" + SystemHelper.makeSlug(by.toString()));
        }
    }

    public void clickElement(By by, int timeout) {
        waitForPageLoaded();
        waitForElementVisible(by, timeout);
        sleep(getStepTime());
        try {
            getWebElement(by).click();
        } catch (ElementClickInterceptedException e) {
            LogUtils.warn("Click intercepted for " + by + ", fallback to JS click");
            ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].click();", getWebElement(by));
        }
        LogUtils.info("Click element " + by);
        if (ConfigManager.isScreenshotStepAllEnabled()) {
            CaptureHelper.takeScreenshot("clickElement_" + SystemHelper.makeSlug(by.toString()));
        }
    }

    // Public Text Input Operations

    public void setText(By by, String value) {
        waitForPageLoaded();
        waitForElementVisible(by);
        sleep(getStepTime());
        getWebElement(by).sendKeys(value);
        LogUtils.info("Set text: " + value + " on element " + by);
        if (ConfigManager.isScreenshotStepAllEnabled()) {
            CaptureHelper.takeScreenshot("setText_" + SystemHelper.makeSlug(by.toString()));
        }
    }

    public void setTextAndKey(By by, String value, Keys key) {
        waitForPageLoaded();
        waitForElementVisible(by);
        sleep(getStepTime());
        getWebElement(by).sendKeys(value, key);
        LogUtils.info("Set text: " + value + " on element " + by);
        if (ConfigManager.isScreenshotStepAllEnabled()) {
            CaptureHelper.takeScreenshot("setText_" + SystemHelper.makeSlug(by.toString()));
        }
    }

    // Public Verification Operations

    public void verifyElementVisible(By by) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(getExplicitTimeout()));
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        LogUtils.info("Verify " + by + " is displayed");
        Assert.assertTrue(DriverManager.getDriver().findElement(by).isDisplayed(), "Element not visible.");
    }

    public void verifyElementVisible(By by, String message) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(getExplicitTimeout()));
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        LogUtils.info("Verify " + by + " is displayed");
        Assert.assertTrue(DriverManager.getDriver().findElement(by).isDisplayed(), message);
    }

    public void verifyTextVisible(String expectedText) {
        By messageLocator = By.xpath("//*[contains(text(),'" + expectedText + "')]");
        waitForElementVisible(messageLocator);
        String actualText = getElementText(messageLocator);
        Assert.assertTrue(actualText.contains(expectedText), "Expected text: " + expectedText + ", Actual text: " + actualText);
    }

    public boolean isElementDisplayed(By by) {
        try {
            LogUtils.info("Verify " + by + " is displayed");
            return DriverManager.getDriver().findElement(by).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean checkElementExist(By by) {
        waitForPageLoaded();
        waitForElementVisible(by);
        List<WebElement> listElement = getWebElements(by);
        if (!listElement.isEmpty()) {
            LogUtils.info("Check Element Exist: " + true + " --- " + by);
            return true;
        } else {
            LogUtils.info("Check Element Exist: " + false + " --- " + by);
            return false;
        }
    }

    // Public Assertions

    public void verifyEquals(Object actual, Object expected) {
        waitForPageLoaded();
        sleep(getStepTime());
        LogUtils.info("Verify equals: " + actual + " and " + expected);
        Assert.assertEquals(actual, expected, "Fail. Not match. '" + actual.toString() + "' != '" + expected.toString() + "'");
    }

    public void verifyEquals(Object actual, Object expected, String message) {
        waitForPageLoaded();
        sleep(getStepTime());
        LogUtils.info("Verify equals: " + actual + " and " + expected);
        Assert.assertEquals(actual, expected, message);
    }

    public void verifyTrue(boolean condition) {
        waitForPageLoaded();
        sleep(getStepTime());
        LogUtils.info("Verify true: " + condition);
        Assert.assertTrue(condition, "Fail, Expected condition to be TRUE, but was FALSE");
    }

    public void verifyTrue(boolean condition, String message) {
        waitForPageLoaded();
        sleep(getStepTime());
        LogUtils.info("Verify true: " + condition);
        Assert.assertTrue(condition, message);
    }

    public void verifyFalse(boolean condition) {
        waitForPageLoaded();
        sleep(getStepTime());
        LogUtils.info("Verify false: " + condition);
        Assert.assertFalse(condition, "Fail, Expected condition to be FALSE, but was TRUE");
    }

    public void verifyFalse(boolean condition, String message) {
        waitForPageLoaded();
        sleep(getStepTime());
        LogUtils.info("Verify false: " + condition);
        Assert.assertFalse(condition, message);
    }

    // Public Wait Operations

    public void waitForElementVisible(By by) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(getExplicitTimeout()), Duration.ofMillis(500));
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Throwable error) {
            LogUtils.error("Timeout waiting for the element Visible. " + by.toString());
            Assert.fail("Timeout waiting for the element Visible. " + by.toString());
        }
    }

    public void waitForElementVisible(By by, int timeOut) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeOut), Duration.ofMillis(500));
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Throwable error) {
            LogUtils.error("Timeout waiting for the element Visible. " + by.toString());
            Assert.fail("Timeout waiting for the element Visible. " + by.toString());
        }
    }

    public void waitForElementPresent(By by) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(getExplicitTimeout()), Duration.ofMillis(500));
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (Throwable error) {
            LogUtils.error("Element not exist. " + by.toString());
            Assert.fail("Element not exist. " + by.toString());
        }
    }

    public void waitForElementPresent(By by, int timeOut) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeOut), Duration.ofMillis(500));
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (Throwable error) {
            LogUtils.error("Element not exist. " + by.toString());
            Assert.fail("Element not exist. " + by.toString());
        }
    }

    public void waitForElementClickable(By by) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(getExplicitTimeout()), Duration.ofMillis(500));
            wait.until(ExpectedConditions.elementToBeClickable(getWebElement(by)));
        } catch (Throwable error) {
            Assert.fail("Timeout waiting for the element ready to click. " + by.toString());
            LogUtils.info("Timeout waiting for the element ready to click. " + by.toString());
        }
    }

    public void waitForElementClickable(By by, int timeOut) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeOut), Duration.ofMillis(500));
            wait.until(ExpectedConditions.elementToBeClickable(getWebElement(by)));
        } catch (Throwable error) {
            Assert.fail("Timeout waiting for the element ready to click. " + by.toString());
            LogUtils.info("Timeout waiting for the element ready to click. " + by.toString());
        }
    }

    public void waitForPageLoaded() {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(getPageLoadTimeout()), Duration.ofMillis(500));
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();

        ExpectedCondition<Boolean> jsLoad = driver -> ((JavascriptExecutor) DriverManager.getDriver()).executeScript("return document.readyState").toString().equals("complete");

        boolean jsReady = js.executeScript("return document.readyState").toString().equals("complete");

        if (!jsReady) {
            LogUtils.info("Javascript in NOT Ready!");
            try {
                wait.until(jsLoad);
            } catch (Throwable error) {
                error.printStackTrace();
                Assert.fail("Timeout waiting for page load (Javascript). (" + getPageLoadTimeout() + "s)");
            }
        }
    }

    public void waitForJQueryLoad() {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(getPageLoadTimeout()), Duration.ofMillis(500));
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();

        ExpectedCondition<Boolean> jQueryLoad = driver -> {
            assert driver != null;
            return ((Long) ((JavascriptExecutor) DriverManager.getDriver()).executeScript("return jQuery.active") == 0);
        };

        boolean jqueryReady = (Boolean) js.executeScript("return jQuery.active==0");

        if (!jqueryReady) {
            LogUtils.info("JQuery is NOT Ready!");
            try {
                wait.until(jQueryLoad);
            } catch (Throwable error) {
                Assert.fail("Timeout waiting for JQuery load. (" + getPageLoadTimeout() + "s)");
            }
        }
    }

    public void waitForAngularLoad() {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(getPageLoadTimeout()), Duration.ofMillis(500));
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        final String angularReadyScript = "return angular.element(document).injector().get('$http').pendingRequests.length === 0";

        ExpectedCondition<Boolean> angularLoad = driver -> {
            assert driver != null;
            return Boolean.valueOf(((JavascriptExecutor) DriverManager.getDriver()).executeScript(angularReadyScript).toString());
        };

        boolean angularReady = Boolean.parseBoolean(js.executeScript(angularReadyScript).toString());

        if (!angularReady) {
            LogUtils.info("Angular is NOT Ready!");
            try {
                wait.until(angularLoad);
            } catch (Throwable error) {
                Assert.fail("Timeout waiting for Angular load. (" + getPageLoadTimeout() + "s)");
            }
        }
    }

    //  Public Scroll Operations

    public void scrollToElement(By by) {
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", getWebElement(by));
    }

    public void scrollToElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("arguments[0].scrollIntoView(false);", element);
        if (ConfigManager.isScreenshotStepAllEnabled()) {
            CaptureHelper.takeScreenshot("scrollToElement_" + SystemHelper.makeSlug(element.getText()));
        }
    }

    public void scrollToElement(WebElement element, String type) {
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("arguments[0].scrollIntoView(" + type + ");", element);
    }

    public void scrollToPosition(int X, int Y) {
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("window.scrollTo(" + X + "," + Y + ");");
    }

    //  Public Mouse / Action Operations
    public boolean moveToElement(By by) {
        try {
            Actions action = new Actions(DriverManager.getDriver());
            action.moveToElement(getWebElement(by)).release(getWebElement(by)).build().perform();
            return true;
        } catch (Exception e) {
            LogUtils.info(e.getMessage());
            return false;
        }
    }

    public boolean moveToOffset(int X, int Y) {
        try {
            Actions action = new Actions(DriverManager.getDriver());
            action.moveByOffset(X, Y).build().perform();
            return true;
        } catch (Exception e) {
            LogUtils.info(e.getMessage());
            return false;
        }
    }

    public boolean hoverElement(By by) {
        try {
            Actions action = new Actions(DriverManager.getDriver());
            action.moveToElement(getWebElement(by)).perform();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean mouseHover(By by) {
        try {
            Actions action = new Actions(DriverManager.getDriver());
            action.moveToElement(getWebElement(by)).perform();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean dragAndDrop(By fromElement, By toElement) {
        try {
            Actions action = new Actions(DriverManager.getDriver());
            action.dragAndDrop(getWebElement(fromElement), getWebElement(toElement)).perform();
            return true;
        } catch (Exception e) {
            LogUtils.info(e.getMessage());
            return false;
        }
    }

    public boolean dragAndDropElement(By fromElement, By toElement) {
        try {
            Actions action = new Actions(DriverManager.getDriver());
            action.clickAndHold(getWebElement(fromElement)).moveToElement(getWebElement(toElement)).release(getWebElement(toElement)).build().perform();
            return true;
        } catch (Exception e) {
            LogUtils.info(e.getMessage());
            return false;
        }
    }

    public boolean dragAndDropOffset(By fromElement, int X, int Y) {
        try {
            Actions action = new Actions(DriverManager.getDriver());
            action.clickAndHold(getWebElement(fromElement)).pause(1).moveByOffset(X, Y).release().build().perform();
            return true;
        } catch (Exception e) {
            LogUtils.info(e.getMessage());
            return false;
        }
    }

    // Public Form Controls

    public void selectCheckBox(By by, String value) {
        WebDriverWait wait = new WebDriverWait(
                DriverManager.getDriver(), Duration.ofSeconds(getExplicitTimeout()), Duration.ofMillis(500));

        WebElement checkBox = wait.until(ExpectedConditions.elementToBeClickable(DriverManager.getDriver().findElement(by)));

        if (value == null || value.equals(checkBox.getAttribute("value"))) {
            if (!checkBox.isSelected()) {
                checkBox.click();
            }
        }
    }

    public void selectRadioButton(By by) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(getExplicitTimeout()), Duration.ofMillis(500));

        WebElement radioButton = wait.until(ExpectedConditions.elementToBeClickable(by));

        if (!radioButton.isSelected()) {
            radioButton.click();
        }
    }

    public void selectDropDown(By by, String value) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(getExplicitTimeout()), Duration.ofMillis(500));
        WebElement dropDownElement = wait.until(ExpectedConditions.elementToBeClickable(by));

        Select select = new Select(dropDownElement);

        for (WebElement option : select.getOptions()) {
            String optionText = option.getText().trim();
            if (optionText.toLowerCase().contains(value.toLowerCase())) {
                option.click();
                return;
            }
        }
    }

    //  Public Keyboard Operations

    public boolean pressENTER() {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean pressESC() {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ESCAPE);
            robot.keyRelease(KeyEvent.VK_ESCAPE);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean pressF11() {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_F11);
            robot.keyRelease(KeyEvent.VK_F11);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //  Public Alert Operations

    public void acceptAlert() {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(getExplicitTimeout()), Duration.ofMillis(500));
            wait.until(ExpectedConditions.alertIsPresent());
            DriverManager.getDriver().switchTo().alert().accept();
            LogUtils.info("Alert accepted");
        } catch (Throwable error) {
            LogUtils.error("No alert present to accept.");
            Assert.fail("No alert present to accept.");
        }
    }

    // Public Toast Operations

    public void verifyToastMessage(String expectedMessage) {
        By toastMessage = By.xpath("//*[contains(text(),'" + expectedMessage + "')]");
        waitForElementVisible(toastMessage);
        String actualMessage = getElementText(toastMessage);
        verifyEquals(actualMessage.trim(), expectedMessage, "Toast message mismatch");
    }

    //  Public Utility Operations

    public void sleep(double second) {
        if (second <= 0) {
            return;
        }
        long nanos = (long) (second * 1_000_000_000L);
        LockSupport.parkNanos(nanos);
    }

    public void logConsole(Object message) {
        System.out.println(message);
    }

    public WebElement highLightElement(By by) {
        if (DriverManager.getDriver() instanceof JavascriptExecutor) {
            ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].style.border='3px solid red'", getWebElement(by));
            sleep(1);
        }
        return getWebElement(by);
    }

    // ObjectUtils Integration

    /**
     * Get By locator from WebElement (PageFactory support)
     */
    public By getByFromWebElement(WebElement element) {
        return ObjectUtils.getByFromWebElement(element);
    }

    /**
     * Parse locator string to By
     * Format: "css selector: .class" or "xpath: //button"
     */
    public By getByLocatorFromString(String locatorToString) {
        return ObjectUtils.getByLocatorFromString(locatorToString);
    }

    /**
     * Get By locator from config properties file
     * Format in config: elementName=type&&value (e.g., LOGIN_BTN=xpath&&//button)
     */
    public By getByLocatorFromConfig(String elementName) {
        return ObjectUtils.getByLocatorFromConfig(elementName);
    }

    /**
     * Get XPath value from config properties
     */
    public String getXpathValue(String elementName) {
        return ObjectUtils.getXpathValue(elementName);
    }

    /**
     * Build dynamic XPath with parameters
     * Usage: getDynamicXpath("//button[normalize-space()='%s']//div[%d]", "Login", 2)
     */
    public String getDynamicXpath(String xpath, Object... values) {
        return ObjectUtils.getXpathDynamic(xpath, values);
    }

    /**
     * Find element by dynamic XPath
     * Usage: findElementByDynamicXpath("//div[%d]", 1)
     */
    public WebElement findElementByDynamicXpath(String xpath, Object... values) {
        String dynamicXpath = getDynamicXpath(xpath, values);
        return getWebElement(By.xpath(dynamicXpath));
    }

    /**
     * Find elements by dynamic XPath
     */
    public List<WebElement> findElementsByDynamicXpath(String xpath, Object... values) {
        String dynamicXpath = getDynamicXpath(xpath, values);
        return getWebElements(By.xpath(dynamicXpath));
    }

    /**
     * Click element by dynamic XPath
     */
    public void clickElementByDynamicXpath(String xpath, Object... values) {
        String dynamicXpath = getDynamicXpath(xpath, values);
        clickElement(By.xpath(dynamicXpath));
    }

    /**
     * Get text from element by dynamic XPath
     */
    public String getTextByDynamicXpath(String xpath, Object... values) {
        String dynamicXpath = getDynamicXpath(xpath, values);
        return getElementText(By.xpath(dynamicXpath));
    }

    /**
     * Set text on element by dynamic XPath
     */
    public void setTextByDynamicXpath(String text, String xpath, Object... values) {
        String dynamicXpath = getDynamicXpath(xpath, values);
        setText(By.xpath(dynamicXpath), text);
    }

    /**
     * Wait and click element by dynamic XPath
     */
    public void waitAndClickByDynamicXpath(String xpath, Object... values) {
        String dynamicXpath = getDynamicXpath(xpath, values);
        By by = By.xpath(dynamicXpath);
        waitForElementClickable(by);
        clickElement(by);
    }
}
