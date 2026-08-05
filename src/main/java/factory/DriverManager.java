package factory;

import org.openqa.selenium.WebDriver;

public class DriverManager {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private DriverManager() {
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void setDriver(WebDriver driver) {
        DriverManager.driver.set(driver);
    }

    public static void quit() {
        WebDriver activeDriver = DriverManager.driver.get();
        if (activeDriver != null) {
            activeDriver.quit();
        }
        driver.remove();
    }

    public static void close() {
        WebDriver activeDriver = DriverManager.driver.get();
        if (activeDriver != null) {
            activeDriver.close();
        }
    }
}