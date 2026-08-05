package helpers;

import factory.DriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.LogUtils;

import java.time.Duration;
import java.util.concurrent.locks.LockSupport;

/**
 * Helper for handling browser popups (password manager, dialogs, etc.)
 */
public class PopupHelper {

    private static void sleep(double seconds) {
        if (seconds <= 0) return;
        LockSupport.parkNanos((long) (seconds * 1_000_000_000L));
    }

    /**
     * Handles the Password Manager popup by pressing the ESC key
     */
    public static void handlePasswordManagerPopup() {
        LogUtils.info("Attempting to handle Password Manager popup by pressing ESC");
        try {
            DriverManager.getDriver().switchTo().activeElement().sendKeys(Keys.ESCAPE);
            Thread.sleep(500);
            LogUtils.info("Password Manager popup closed successfully with ESC");
        } catch (Exception e) {
            LogUtils.warn("Failed to close popup with ESC: " + e.getMessage());
        }
    }

    /**
     * Handles the Password Manager popup by pressing the ESC key multiple times
     */
    public static void handlePasswordManagerPopupMultipleTimes() {
        LogUtils.info("Attempting to handle Password Manager popup by pressing ESC multiple times");
        try {
            for (int i = 0; i < 3; i++) {
                DriverManager.getDriver().switchTo().activeElement().sendKeys(Keys.ESCAPE);
                Thread.sleep(300);
            }
            LogUtils.info("Password Manager popup closed successfully with multiple ESC presses");
        } catch (Exception e) {
            LogUtils.warn("Failed to close popup with multiple ESC: " + e.getMessage());
        }
    }

    /**
     * Handles the Password Manager popup by locating and clicking the "Not now" or "Skip" button
     */
    public static void handlePasswordManagerPopupByClickingButton() {
        LogUtils.info("Attempting to handle Password Manager popup by clicking close button");
        try {
            String[] possibleButtons = {
                    "//button[contains(text(), 'Not now')]",
                    "//button[contains(text(), 'Skip')]",
                    "//button[@aria-label='Close']",
                    "//button[@class*='close']",
                    "//div[@role='dialog']//button[1]"
            };

            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(2));

            for (String buttonXPath : possibleButtons) {
                try {
                    WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(buttonXPath)));
                    button.click();
                    LogUtils.info("Password Manager popup closed by clicking: " + buttonXPath);
                    Thread.sleep(500);
                    return;
                } catch (TimeoutException | NoSuchElementException e) {
                    // Continue to next button
                }
            }
            LogUtils.warn("Failed to find close button for Password Manager popup");
        } catch (Exception e) {
            LogUtils.warn("Failed to close popup by clicking button: " + e.getMessage());
        }
    }

    /**
     * Handles the Password Manager popup using a combination of ESC key and button click
     */
    public static void handlePasswordManagerPopupCombined() {
        LogUtils.info("Attempting to handle Password Manager popup with combined method (ESC + Click)");
        handlePasswordManagerPopup();
        sleep(1);
        handlePasswordManagerPopupByClickingButton();
        LogUtils.info("Password Manager popup handling completed");
    }

    /**
     * Handles all possible popups that may appear
     */
    public static void handleAllPopups() {
        LogUtils.info("Attempting to handle all possible popups");
        try {
            handlePasswordManagerPopup();
            sleep(0.5);
            handlePasswordManagerPopupByClickingButton();
            sleep(0.5);
            handlePasswordManagerPopup();
            LogUtils.info("All popup handling attempts completed");
        } catch (Exception e) {
            LogUtils.warn("Error during popup handling: " + e.getMessage());
        }
    }

    /**
     * Waits for the popup to disappear
     */
    public static void waitForPopupToDisappear() {
        LogUtils.info("Waiting for popup to disappear");
        try {
            Thread.sleep(1000);

            boolean popupExists = !DriverManager.getDriver().findElements(
                    By.xpath("//div[@role='dialog'] | //div[@class*='popup'] | //div[@class*='modal']")
            ).isEmpty();

            if (popupExists) {
                LogUtils.info("Popup detected, handling it");
                handlePasswordManagerPopupCombined();
            }

            Thread.sleep(500);
            LogUtils.info("Popup handling completed");
        } catch (Exception e) {
            LogUtils.warn("Error waiting for popup: " + e.getMessage());
        }
    }
}
