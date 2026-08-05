package strategies;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Strategy interface for element finding across different platforms (Web, Mobile, etc.)
 */
public interface ElementFinderStrategy {

    WebElement findElement(By by);

    List<WebElement> findElements(By by);

    void click(WebElement element);

    void sendKeys(WebElement element, String value);

    boolean isDisplayed(WebElement element);

    String getText(WebElement element);
}
