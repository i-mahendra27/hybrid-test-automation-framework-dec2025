package utils;

import factory.DriverManager;
import managers.ConfigManager;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

public class BrowserInfoUtils {
    private BrowserInfoUtils() {
        super();
    }

    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static volatile String lastBrowserInfo;

    public static void captureBrowserInfo(WebDriver driver) {
        String browserInfo = getBrowserInfoFromDriver(driver);
        if (browserInfo != null && !browserInfo.isBlank()) {
            lastBrowserInfo = browserInfo;
        }
    }

    public static String getBrowserInfo() {
        if (lastBrowserInfo != null && !lastBrowserInfo.isBlank()) {
            return lastBrowserInfo;
        }

        String browserInfo = getBrowserInfoFromDriver(DriverManager.getDriver());
        if (browserInfo != null && !browserInfo.isBlank()) {
            lastBrowserInfo = browserInfo;
            return browserInfo;
        }

        String browser = getConfiguredBrowser();
        String browserVersion = detectBrowserVersion(browser);
        if (browserVersion.isBlank()) {
            return browser;
        }
        return browser + " " + browserVersion;
    }

    public static String getOSInfo() {
        return System.getProperty("os.name");
    }

    public static boolean isWindows() {
        return (OS.contains("win"));
    }

    public static boolean isMac() {
        return (OS.contains("mac"));
    }

    public static boolean isUnix() {
        return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
    }

    public static boolean isSolaris() {
        return (OS.contains("sunos"));
    }

    private static String getBrowserInfoFromDriver(WebDriver driver) {
        if (!(driver instanceof HasCapabilities hasCapabilities)) {
            return "";
        }

        Capabilities capabilities = hasCapabilities.getCapabilities();
        String browserName = valueOrBlank(capabilities.getBrowserName());
        String browserVersion = valueOrBlank(capabilities.getBrowserVersion());
        if (browserName.isBlank()) {
            return "";
        }
        String formattedBrowserName = formatBrowserName(browserName);
        return browserVersion.isBlank() ? formattedBrowserName : formattedBrowserName + " " + browserVersion;
    }

    private static String getConfiguredBrowser() {
        try {
            String testBrowser = Reporter.getCurrentTestResult()
                    .getTestContext()
                    .getCurrentXmlTest()
                    .getParameter("BROWSER");
            if (testBrowser != null && !testBrowser.trim().isEmpty()) {
                return formatBrowserName(testBrowser);
            }
        } catch (Exception ignored) {
            // Reporter context is not available after suite finish.
        }
        return formatBrowserName(ConfigManager.getBrowser());
    }

    private static String detectBrowserVersion(String browser) {
        String normalizedBrowser = browser.toLowerCase();
        String[] commands = switch (normalizedBrowser) {
            case "chrome" -> new String[]{"chrome --version", "google-chrome --version", "chromium --version"};
            case "edge" -> new String[]{"msedge --version", "microsoft-edge --version"};
            case "firefox" -> new String[]{"firefox --version"};
            default -> new String[0];
        };

        for (String command : commands) {
            String version = runVersionCommand(command);
            if (!version.isBlank()) {
                return version.replaceFirst("(?i)^.*?([0-9]+(\\.[0-9]+)+).*$", "$1");
            }
        }
        return "";
    }

    private static String runVersionCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            if (!process.waitFor(2, java.util.concurrent.TimeUnit.SECONDS) || process.exitValue() != 0) {
                return "";
            }
            return new String(process.getInputStream().readAllBytes()).trim();
        } catch (Exception ignored) {
            return "";
        }
    }

    private static String formatBrowserName(String browser) {
        if (browser == null || browser.trim().isEmpty()) {
            return "N/A";
        }
        String normalizedBrowser = browser.trim().toLowerCase();
        return switch (normalizedBrowser) {
            case "chrome" -> "Chrome";
            case "firefox" -> "Firefox";
            case "edge" -> "Edge";
            default -> browser.trim();
        };
    }

    private static String valueOrBlank(String value) {
        return value == null ? "" : value.trim();
    }
}
