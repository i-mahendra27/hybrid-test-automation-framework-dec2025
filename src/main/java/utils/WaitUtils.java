package utils;

import factory.DriverManager;
import managers.ConfigManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class WaitUtils {
    private static final int DEFAULT_TIMEOUT = ConfigManager.getExplicitWaitTimeout();
    private static final int DEFAULT_POLLING = 500;

    public static WebDriverWait getWait(int timeoutInSeconds) {
        return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutInSeconds));
    }

    public static WebDriverWait getWait() {
        return getWait(DEFAULT_TIMEOUT);
    }

    public static FluentWait<WebDriver> getFluentWait(int timeoutInSeconds, int pollingInMillis) {
        return new FluentWait<>(DriverManager.getDriver())
                .withTimeout(Duration.ofSeconds(timeoutInSeconds))
                .pollingEvery(Duration.ofMillis(pollingInMillis))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(ElementNotInteractableException.class);
    }

    public static FluentWait<WebDriver> getFluentWait() {
        return getFluentWait(DEFAULT_TIMEOUT, DEFAULT_POLLING);
    }

    public static WebElement waitForElementVisible(By locator, int timeout) {
        try {
            return getWait(timeout).until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            LogUtils.error("Element not visible after " + timeout + " seconds: " + locator);
            throw e;
        }
    }

    public static WebElement waitForElementVisible(By locator) {
        return waitForElementVisible(locator, DEFAULT_TIMEOUT);
    }

    public static WebElement waitForElementPresent(By locator, int timeout) {
        try {
            return getWait(timeout).until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (TimeoutException e) {
            LogUtils.error("Element not present after " + timeout + " seconds: " + locator);
            throw e;
        }
    }

    public static WebElement waitForElementPresent(By locator) {
        return waitForElementPresent(locator, DEFAULT_TIMEOUT);
    }

    public static WebElement waitForElementClickable(By locator, int timeout) {
        try {
            return getWait(timeout).until(ExpectedConditions.elementToBeClickable(locator));
        } catch (TimeoutException e) {
            LogUtils.error("Element not clickable after " + timeout + " seconds: " + locator);
            throw e;
        }
    }

    public static WebElement waitForElementClickable(By locator) {
        return waitForElementClickable(locator, DEFAULT_TIMEOUT);
    }

    public static WebElement waitForElementClickable(WebElement element, int timeout) {
        try {
            return getWait(timeout).until(ExpectedConditions.elementToBeClickable(element));
        } catch (TimeoutException e) {
            LogUtils.error("Element not clickable after " + timeout + " seconds");
            throw e;
        }
    }

    public static WebElement waitForElementClickable(WebElement element) {
        return waitForElementClickable(element, DEFAULT_TIMEOUT);
    }

    public static boolean waitForElementInvisible(By locator, int timeout) {
        try {
            return getWait(timeout).until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            LogUtils.error("Element still visible after " + timeout + " seconds: " + locator);
            return false;
        }
    }

    public static boolean waitForElementInvisible(By locator) {
        return waitForElementInvisible(locator, DEFAULT_TIMEOUT);
    }

    public static List<WebElement> waitForElementsVisible(By locator, int timeout) {
        try {
            return getWait(timeout).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
        } catch (TimeoutException e) {
            LogUtils.error("Elements not visible after " + timeout + " seconds: " + locator);
            throw e;
        }
    }

    public static List<WebElement> waitForElementsVisible(By locator) {
        return waitForElementsVisible(locator, DEFAULT_TIMEOUT);
    }

    public static List<WebElement> waitForElementsPresent(By locator, int timeout) {
        try {
            return getWait(timeout).until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        } catch (TimeoutException e) {
            LogUtils.error("Elements not present after " + timeout + " seconds: " + locator);
            throw e;
        }
    }

    public static List<WebElement> waitForElementsPresent(By locator) {
        return waitForElementsPresent(locator, DEFAULT_TIMEOUT);
    }

    public static boolean waitForTextToBePresentInElement(By locator, String text, int timeout) {
        try {
            return getWait(timeout).until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
        } catch (TimeoutException e) {
            LogUtils.error("Text '" + text + "' not present in element after " + timeout + " seconds: " + locator);
            return false;
        }
    }

    public static boolean waitForTextToBePresentInElement(By locator, String text) {
        return waitForTextToBePresentInElement(locator, text, DEFAULT_TIMEOUT);
    }

    public static boolean waitForAttributeContains(By locator, String attribute, String value, int timeout) {
        try {
            return getWait(timeout).until(ExpectedConditions.attributeContains(locator, attribute, value));
        } catch (TimeoutException e) {
            LogUtils.error("Attribute '" + attribute + "' does not contain '" + value + "' after " + timeout + " seconds: " + locator);
            return false;
        }
    }

    public static boolean waitForAttributeContains(By locator, String attribute, String value) {
        return waitForAttributeContains(locator, attribute, value, DEFAULT_TIMEOUT);
    }

    public static boolean waitForAttributeToBe(By locator, String attribute, String value, int timeout) {
        try {
            return getWait(timeout).until(ExpectedConditions.attributeToBe(locator, attribute, value));
        } catch (TimeoutException e) {
            LogUtils.error("Attribute '" + attribute + "' is not '" + value + "' after " + timeout + " seconds: " + locator);
            return false;
        }
    }

    public static boolean waitForAttributeToBe(By locator, String attribute, String value) {
        return waitForAttributeToBe(locator, attribute, value, DEFAULT_TIMEOUT);
    }

    public static boolean waitForUrlContains(String urlFragment, int timeout) {
        try {
            return getWait(timeout).until(ExpectedConditions.urlContains(urlFragment));
        } catch (TimeoutException e) {
            LogUtils.error("URL does not contain '" + urlFragment + "' after " + timeout + " seconds");
            return false;
        }
    }

    public static boolean waitForUrlContains(String urlFragment) {
        return waitForUrlContains(urlFragment, DEFAULT_TIMEOUT);
    }

    public static boolean waitForUrlToBe(String url, int timeout) {
        try {
            return getWait(timeout).until(ExpectedConditions.urlToBe(url));
        } catch (TimeoutException e) {
            LogUtils.error("URL is not '" + url + "' after " + timeout + " seconds");
            return false;
        }
    }

    public static boolean waitForUrlToBe(String url) {
        return waitForUrlToBe(url, DEFAULT_TIMEOUT);
    }

    public static boolean waitForTitleContains(String title, int timeout) {
        try {
            return getWait(timeout).until(ExpectedConditions.titleContains(title));
        } catch (TimeoutException e) {
            LogUtils.error("Title does not contain '" + title + "' after " + timeout + " seconds");
            return false;
        }
    }

    public static boolean waitForTitleContains(String title) {
        return waitForTitleContains(title, DEFAULT_TIMEOUT);
    }

    public static Alert waitForAlertPresent(int timeout) {
        try {
            return getWait(timeout).until(ExpectedConditions.alertIsPresent());
        } catch (TimeoutException e) {
            LogUtils.error("Alert not present after " + timeout + " seconds");
            throw e;
        }
    }

    public static Alert waitForAlertPresent() {
        return waitForAlertPresent(DEFAULT_TIMEOUT);
    }

    public static boolean waitForElementToBeSelected(By locator, int timeout) {
        try {
            return getWait(timeout).until(ExpectedConditions.elementToBeSelected(locator));
        } catch (TimeoutException e) {
            LogUtils.error("Element not selected after " + timeout + " seconds: " + locator);
            return false;
        }
    }

    public static boolean waitForElementToBeSelected(By locator) {
        return waitForElementToBeSelected(locator, DEFAULT_TIMEOUT);
    }

    public static boolean waitForStalenessOf(WebElement element, int timeout) {
        try {
            return getWait(timeout).until(ExpectedConditions.stalenessOf(element));
        } catch (TimeoutException e) {
            LogUtils.error("Element is not stale after " + timeout + " seconds");
            return false;
        }
    }

    public static boolean waitForStalenessOf(WebElement element) {
        return waitForStalenessOf(element, DEFAULT_TIMEOUT);
    }

    public static List<WebElement> waitForNumberOfElementsToBe(By locator, int number, int timeout) {
        try {
            getWait(timeout).until(ExpectedConditions.numberOfElementsToBe(locator, number));
            return DriverManager.getDriver().findElements(locator);
        } catch (TimeoutException e) {
            LogUtils.error("Number of elements is not " + number + " after " + timeout + " seconds: " + locator);
            throw e;
        }
    }

    public static List<WebElement> waitForNumberOfElementsToBe(By locator, int number) {
        return waitForNumberOfElementsToBe(locator, number, DEFAULT_TIMEOUT);
    }

    public static List<WebElement> waitForNumberOfElementsToBeMoreThan(By locator, int number, int timeout) {
        try {
            getWait(timeout).until(ExpectedConditions.numberOfElementsToBeMoreThan(locator, number));
            return DriverManager.getDriver().findElements(locator);
        } catch (TimeoutException e) {
            LogUtils.error("Number of elements is not more than " + number + " after " + timeout + " seconds: " + locator);
            throw e;
        }
    }

    public static List<WebElement> waitForNumberOfElementsToBeMoreThan(By locator, int number) {
        return waitForNumberOfElementsToBeMoreThan(locator, number, DEFAULT_TIMEOUT);
    }

    public static List<WebElement> waitForNumberOfElementsToBeLessThan(By locator, int number, int timeout) {
        try {
            getWait(timeout).until(ExpectedConditions.numberOfElementsToBeLessThan(locator, number));
            return DriverManager.getDriver().findElements(locator);
        } catch (TimeoutException e) {
            LogUtils.error("Number of elements is not less than " + number + " after " + timeout + " seconds: " + locator);
            throw e;
        }
    }

    public static List<WebElement> waitForNumberOfElementsToBeLessThan(By locator, int number) {
        return waitForNumberOfElementsToBeLessThan(locator, number, DEFAULT_TIMEOUT);
    }

    public static WebElement waitForElementRefreshed(By locator, int timeout) {
        try {
            return getWait(timeout).until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(locator)));
        } catch (TimeoutException e) {
            LogUtils.error("Element not refreshed after " + timeout + " seconds: " + locator);
            throw e;
        }
    }

    public static WebElement waitForElementRefreshed(By locator) {
        return waitForElementRefreshed(locator, DEFAULT_TIMEOUT);
    }

    public static <T> T waitForCondition(Function<WebDriver, T> condition, int timeout) {
        try {
            return getWait(timeout).until(condition);
        } catch (TimeoutException e) {
            LogUtils.error("Custom condition not met after " + timeout + " seconds");
            throw e;
        }
    }

    public static <T> T waitForCondition(Function<WebDriver, T> condition) {
        return waitForCondition(condition, DEFAULT_TIMEOUT);
    }

    public static <T> T fluentWaitForCondition(Function<WebDriver, T> condition, int timeout, int polling) {
        try {
            return getFluentWait(timeout, polling).until(condition);
        } catch (TimeoutException e) {
            LogUtils.error("Fluent wait condition not met after " + timeout + " seconds");
            throw e;
        }
    }

    public static <T> T fluentWaitForCondition(Function<WebDriver, T> condition) {
        return fluentWaitForCondition(condition, DEFAULT_TIMEOUT, DEFAULT_POLLING);
    }

    public static boolean waitForJQueryToLoad(int timeout) {
        try {
            return getWait(timeout).until((ExpectedCondition<Boolean>) driver -> {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                return (Boolean) js.executeScript("return jQuery.active == 0");
            });
        } catch (Exception e) {
            LogUtils.warn("jQuery not loaded or not present on page");
            return true;
        }
    }

    public static boolean waitForJQueryToLoad() {
        return waitForJQueryToLoad(DEFAULT_TIMEOUT);
    }

    public static boolean waitForPageLoad(int timeout) {
        try {
            return getWait(timeout).until((ExpectedCondition<Boolean>) driver -> {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                return js.executeScript("return document.readyState").toString().equals("complete");
            });
        } catch (TimeoutException e) {
            LogUtils.error("Page not loaded after " + timeout + " seconds");
            return false;
        }
    }

    public static boolean waitForPageLoad() {
        return waitForPageLoad(ConfigManager.getPageLoadTimeout());
    }

    public static boolean waitForAngularLoad(int timeout) {
        try {
            return getWait(timeout).until((ExpectedCondition<Boolean>) driver -> {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                return (Boolean) js.executeScript("return window.getAllAngularTestabilities().findIndex(x=>!x.isStable()) === -1");
            });
        } catch (Exception e) {
            LogUtils.warn("Angular not loaded or not present on page");
            return true;
        }
    }

    public static boolean waitForAngularLoad() {
        return waitForAngularLoad(DEFAULT_TIMEOUT);
    }

    public static void hardWait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LogUtils.error("Hard wait interrupted: " + e.getMessage());
        }
    }

    public static void hardWait() {
        hardWait(ConfigManager.getHardWaitTimeout() * 1000);
    }
}
