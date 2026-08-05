package strategies;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Mobile implementation using Appium
 * Placeholder - extend with actual Appium MobileElement logic
 */
public class MobileElementFinderStrategy implements ElementFinderStrategy {

    @Override
    public WebElement findElement(By by) {
        // TODO: Replace with Appium driver.findElement(by)
        throw new UnsupportedOperationException(
            "MobileElementFinderStrategy not yet configured. " +
            "Implement Appium driver integration."
        );
    }

    @Override
    public List<WebElement> findElements(By by) {
        // TODO: Replace with Appium driver.findElements(by)
        throw new UnsupportedOperationException(
            "MobileElementFinderStrategy not yet configured. " +
            "Implement Appium driver integration."
        );
    }

    @Override
    public void click(WebElement element) {
        // TODO: Replace with Appium tap() or click()
        throw new UnsupportedOperationException(
            "MobileElementFinderStrategy not yet configured. " +
            "Implement Appium click/tap integration."
        );
    }

    @Override
    public void sendKeys(WebElement element, String value) {
        // TODO: Replace with Appium setValue() or sendKeys()
        throw new UnsupportedOperationException(
            "MobileElementFinderStrategy not yet configured. " +
            "Implement Appium input integration."
        );
    }

    @Override
    public boolean isDisplayed(WebElement element) {
        return element.isDisplayed();
    }

    @Override
    public String getText(WebElement element) {
        return element.getText();
    }
}
