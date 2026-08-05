package strategies;

import factory.DriverManager;
import org.openqa.selenium.*;
import utils.LogUtils;

import java.util.List;

/**
 * Web implementation using Selenium WebDriver
 */
public class WebElementFinderStrategy implements ElementFinderStrategy {

    @Override
    public WebElement findElement(By by) {
        return DriverManager.getDriver().findElement(by);
    }

    @Override
    public List<WebElement> findElements(By by) {
        return DriverManager.getDriver().findElements(by);
    }

    @Override
    public void click(WebElement element) {
        try {
            element.click();
        } catch (ElementClickInterceptedException e) {
            LogUtils.warn("Click intercepted for element, fallback to JS click");
            ((JavascriptExecutor) DriverManager.getDriver())
                    .executeScript("arguments[0].click();", element);
        }
    }

    @Override
    public void sendKeys(WebElement element, String value) {
        element.sendKeys(value);
    }

    @Override
    public boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @Override
    public String getText(WebElement element) {
        return element.getText();
    }
}
