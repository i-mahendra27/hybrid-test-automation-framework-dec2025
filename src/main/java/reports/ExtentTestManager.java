package reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import enums.CategoryType;
import factory.DriverManager;
import managers.ConfigManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExtentTestManager {
    // Use ConcurrentHashMap if UI and API operations run in parallel.
    // Use HashMap if only one of them (UI or API) is used.
    static Map<Integer, ExtentTest> extentTestMap = new ConcurrentHashMap<>();

    public static ExtentTest getTest() {
        return extentTestMap.get((int) Thread.currentThread().getId());
    }

    public static synchronized ExtentTest createTest(String testName) {
        if (!ConfigManager.isExtentReportEnabled()) {
            return null;
        }
        ExtentReports extentReports = ExtentReportManager.getExtentReports();
        if (extentReports == null) {
            return null;
        }
        ExtentTest test = extentReports.createTest(testName);
        extentTestMap.put((int) Thread.currentThread().getId(), test);
        return test;
    }

    public static synchronized ExtentTest createTest(String testName, String desc) {
        if (!ConfigManager.isExtentReportEnabled()) {
            return null;
        }
        ExtentReports extentReports = ExtentReportManager.getExtentReports();
        if (extentReports == null) {
            return null;
        }
        ExtentTest test = extentReports.createTest(testName, desc);
        extentTestMap.put((int) Thread.currentThread().getId(), test);
        return test;
    }

    public static synchronized ExtentTest createTest(String testName, String desc, CategoryType category) {
        if (!ConfigManager.isExtentReportEnabled()) {
            return null;
        }
        ExtentReports extentReports = ExtentReportManager.getExtentReports();
        if (extentReports == null) {
            return null;
        }
        ExtentTest test = extentReports.createTest(testName, desc);
        if (category != null) {
            test.assignCategory(category.toString());
        }
        extentTestMap.put((int) Thread.currentThread().getId(), test);
        return test;
    }

    public static synchronized ExtentTest createTest(String testName, CategoryType category) {
        if (!ConfigManager.isExtentReportEnabled()) {
            return null;
        }
        ExtentReports extentReports = ExtentReportManager.getExtentReports();
        if (extentReports == null) {
            return null;
        }
        ExtentTest test = extentReports.createTest(testName);
        if (category != null) {
            test.assignCategory(category.toString());
        }
        extentTestMap.put((int) Thread.currentThread().getId(), test);
        return test;
    }

    public static synchronized ExtentTest saveToReport(String testName, String desc) {
        if (!ConfigManager.isExtentReportEnabled()) {
            return null;
        }
        ExtentReports extentReports = ExtentReportManager.getExtentReports();
        if (extentReports == null) {
            return null;
        }
        ExtentTest test = extentReports.createTest(testName, desc);
        extentTestMap.put((int) Thread.currentThread().getId(), test);
        return test;
    }

    public static void addScreenShot(String message) {
        if (!ConfigManager.isExtentReportEnabled() || getTest() == null) {
            return;
        }
        String base64Image = "data:image/png;base64,"
                + ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BASE64);

        getTest().log(Status.INFO, message,
                MediaEntityBuilder.createScreenCaptureFromBase64String(base64Image).build());
    }

    public static void addScreenShot(Status status, String message) {
        if (!ConfigManager.isExtentReportEnabled() || getTest() == null) {
            return;
        }
        String base64Image = "data:image/png;base64,"
                + ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BASE64);

        getTest().log(status, message,
                MediaEntityBuilder.createScreenCaptureFromBase64String(base64Image).build());
    }

    public static void logMessage(String message) {
        if (!ConfigManager.isExtentReportEnabled() || getTest() == null) {
            return;
        }
        getTest().log(Status.INFO, message);
    }

    public static void logMessage(Status status, String message) {
        if (!ConfigManager.isExtentReportEnabled() || getTest() == null) {
            return;
        }
        getTest().log(status, message);
    }

    public static ExtentReports getExtentReports() {
        return ExtentReportManager.getExtentReports();
    }
}
