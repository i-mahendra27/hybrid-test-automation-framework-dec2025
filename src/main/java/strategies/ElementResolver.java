package strategies;

import enums.PlatformType;
import managers.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

import java.util.List;
import java.util.Objects;

/**
 * Resolver for element finding strategies.
 * Switches between Web/Mobile strategies based on configuration.
 *
 * Usage:
 *   ElementResolver.getInstance().findElement(By.xpath("..."));
 *   ElementResolver.getInstance().switchStrategy(PlatformType.MOBILE);
 */
public class ElementResolver {

    private static volatile ElementResolver instance;
    private ElementFinderStrategy currentStrategy;
    private PlatformType currentPlatform = PlatformType.WEB;

    private ElementResolver() {
        initializeStrategy();
    }

    public static ElementResolver getInstance() {
        if (instance == null) {
            synchronized (ElementResolver.class) {
                if (instance == null) {
                    instance = new ElementResolver();
                }
            }
        }
        return instance;
    }

    private void initializeStrategy() {
        try {
            String platform = ConfigManager.getPlatform();
            switchStrategy(PlatformType.valueOf(platform.toUpperCase()));
        } catch (Exception e) {
            LogUtils.warn("Could not read platform config, defaulting to WEB");
            switchStrategy(PlatformType.WEB);
        }
    }

    public void switchStrategy(PlatformType platformType) {
        if (Objects.requireNonNull(platformType) == PlatformType.WEB) {
            currentStrategy = new WebElementFinderStrategy();
            currentPlatform = PlatformType.WEB;
            LogUtils.info("Element strategy switched to WEB");
        } else {
            throw new IllegalArgumentException("Unknown platform type: " + platformType);
        }
    }

    public ElementFinderStrategy getCurrentStrategy() {
        return currentStrategy;
    }

    public PlatformType getCurrentPlatform() {
        return currentPlatform;
    }

    // Delegated methods for direct usage

    public WebElement findElement(By by) {
        return currentStrategy.findElement(by);
    }

    public List<WebElement> findElements(By by) {
        return currentStrategy.findElements(by);
    }

    public void click(WebElement element) {
        currentStrategy.click(element);
    }

    public void sendKeys(WebElement element, String value) {
        currentStrategy.sendKeys(element, value);
    }

    public boolean isDisplayed(WebElement element) {
        return currentStrategy.isDisplayed(element);
    }

    public String getText(WebElement element) {
        return currentStrategy.getText(element);
    }
}
