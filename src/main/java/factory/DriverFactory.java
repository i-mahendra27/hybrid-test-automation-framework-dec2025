package factory;

import enums.Browser;
import helpers.PropertiesHelper;
import managers.ConfigManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import utils.BrowserInfoUtils;
import utils.LogUtils;

import java.util.HashMap;
import java.util.Map;

public class DriverFactory {
    static{
        PropertiesHelper.loadAllFiles();
    }

    public WebDriver createDriver() {
        WebDriver driver = setupBrowser(ConfigManager.getBrowser());
        DriverManager.setDriver(driver);
        return driver;
    }

    public WebDriver createDriver(String browserName) {
        WebDriver driver = setupBrowser(browserName);
        DriverManager.setDriver(driver);
        return driver;
    }

    private WebDriver setupBrowser(String browserName) {
        Browser browser = parseBrowser(browserName);
        WebDriver driver = switch (browser) {
            case CHROME -> initChromeDriver();
            case FIREFOX -> initFirefoxDriver();
            case EDGE -> initEdgeDriver();
        };
        BrowserInfoUtils.captureBrowserInfo(driver);
        return driver;
    }

    private Browser parseBrowser(String browserName) {
        if (browserName == null || browserName.isBlank()) {
            return Browser.CHROME;
        }
        String normalized = browserName.trim().toLowerCase();
        try {
            return Browser.valueOf(normalized.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Browser: " + browserName + " is invalid, Launching Chrome browser default...");
            return Browser.CHROME;
        }
    }

    private WebDriver initChromeDriver() {
        WebDriver driver;
        LogUtils.info("Launching Chrome browser...");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-password-manager-reauthentication");
        options.addArguments("--disable-notifications");
        options.setExperimentalOption("excludeSwitches", new String[]{"disable-popup-blocking"});
        options.addArguments("--disable-popup-blocking");

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        if (ConfigManager.isHeadless()) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1800,900");
            LogUtils.info("Headless mode On");
        } else {
            options.addArguments("--start-maximized");
            LogUtils.info("Headless mode Off");
        }
        driver = new ChromeDriver(options);
        return driver;
    }

    private WebDriver initEdgeDriver() {
        WebDriver driver;
        LogUtils.info("Launching Edge browser...");

        EdgeOptions options = new EdgeOptions();
        options.addArguments("--disable-password-manager-reauthentication");
        options.addArguments("--disable-notifications");
        options.setExperimentalOption("excludeSwitches", new String[]{"disable-popup-blocking"});
        options.addArguments("--disable-popup-blocking");

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        if (ConfigManager.isHeadless()) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1800,900");
            LogUtils.info("Headless mode On");
        } else {
            options.addArguments("--start-maximized");
            LogUtils.info("Headless mode Off");
        }

        driver = new EdgeDriver(options);

        return driver;
    }

    private WebDriver initFirefoxDriver() {
        WebDriver driver;
        LogUtils.info("Launching Firefox browser...");

        FirefoxOptions options = new FirefoxOptions();
        options.addPreference("signon.rememberSignons", false);
        options.addPreference("signon.generation.enabled", false);
        options.addPreference("browser.startup.homepage_override.mstone", "ignore");
        options.addPreference("startup.homepage_welcome_url", "");
        options.addPreference("startup.homepage_welcome_url.additional", "");
        // Disable notifications
        options.addPreference("permissions.default.desktop-notification", 2);

        if (ConfigManager.isHeadless()) {
            options.addArguments("--headless");
            LogUtils.info("Headless mode On");
        } else {
            options.addArguments("--start-maximized");
            LogUtils.info("Headless mode Off");
        }

        driver = new FirefoxDriver(options);
        if (ConfigManager.isHeadless()) {
            driver.manage().window().setSize(new org.openqa.selenium.Dimension(1800, 900));
        }

        return driver;
    }
}
