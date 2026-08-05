package listeners;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import managers.ConfigManager;
import reports.ExtentTestManager;
import utils.LogUtils;
import factory.DriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CucumberReportListener implements ConcurrentEventListener {
    private static final String SEPARATOR = "=".repeat(40);
    private static final String SUB_SEPARATOR = "-".repeat(40);

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestRunStarted.class, this::handleTestRunStarted);
        publisher.registerHandlerFor(TestRunFinished.class, this::handleTestRunFinished);
        publisher.registerHandlerFor(TestCaseStarted.class, this::handleTestCaseStarted);
        publisher.registerHandlerFor(TestCaseFinished.class, this::handleTestCaseFinished);
        publisher.registerHandlerFor(TestStepStarted.class, this::handleTestStepStarted);
        publisher.registerHandlerFor(TestStepFinished.class, this::handleTestStepFinished);
    }

    private void handleTestRunStarted(TestRunStarted event) {
        LogUtils.info("\n" + SEPARATOR);
        LogUtils.info("TEST RUN STARTED");
        LogUtils.info("Time: " + event.getInstant());
        LogUtils.info(SEPARATOR);

        if (!ConfigManager.isExtentReportEnabled()) {
            LogUtils.info("ExtentReport disabled by config");
            return;
        }

        // Initialize ExtentReport
        try {
            ExtentTestManager.getExtentReports();
            LogUtils.info("ExtentReport initialized successfully");
        } catch (Exception e) {
            LogUtils.error("Failed to initialize ExtentReport: " + e.getMessage());
        }
    }

    private void handleTestRunFinished(TestRunFinished event) {
        LogUtils.info("\n" + SEPARATOR);
        LogUtils.info("TEST RUN FINISHED");
        LogUtils.info("Time: " + event.getInstant());
        LogUtils.info(SEPARATOR + "\n");

        if (!ConfigManager.isExtentReportEnabled()) {
            return;
        }

        // Flush Extent Report
        try {
            if (ExtentTestManager.getExtentReports() != null) {
                LogUtils.info("Flushing Extent Report...");
                ExtentTestManager.getExtentReports().flush();
                LogUtils.info("Extent Report flushed successfully");
                LogUtils.info("Please check the exports/ExtentReport folder for the HTML report");
            } else {
                LogUtils.error("ExtentReports instance is null, cannot flush");
            }
        } catch (Exception e) {
            LogUtils.error("Failed to flush Extent Report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleTestCaseStarted(TestCaseStarted event) {
        TestCase testCase = event.getTestCase();
        LogUtils.info("\nTEST CASE STARTED: " + testCase.getName());
        LogUtils.info("  Feature: " + testCase.getUri());
        LogUtils.info("  Line: " + testCase.getLocation().getLine());
    }

    private void handleTestCaseFinished(TestCaseFinished event) {
        TestCase testCase = event.getTestCase();
        io.cucumber.plugin.event.Status status = event.getResult().getStatus();

        LogUtils.info("\nTEST CASE FINISHED: " + testCase.getName());
        LogUtils.info("  Status: " + status.name());
        LogUtils.info("  Duration: " + event.getResult().getDuration().toMillis() + " ms");

        if (status == io.cucumber.plugin.event.Status.FAILED) {
            Throwable error = event.getResult().getError();
            if (error != null) {
                LogUtils.error("  Error: " + error.getMessage());
            }
        }
        LogUtils.info(SUB_SEPARATOR);
    }

    private void handleTestStepStarted(TestStepStarted event) {
        TestStep testStep = event.getTestStep();
        if (testStep instanceof PickleStepTestStep) {
            PickleStepTestStep pickleStep = (PickleStepTestStep) testStep;
            String stepText = pickleStep.getStep().getText();
            LogUtils.info("  Step: " + stepText);

            // Removed: ExtentReport logging for "Executing step" to avoid duplication
        }
    }

    private void handleTestStepFinished(TestStepFinished event) {
        TestStep testStep = event.getTestStep();
        io.cucumber.plugin.event.Status status = event.getResult().getStatus();

        if (testStep instanceof PickleStepTestStep) {
            PickleStepTestStep pickleStep = (PickleStepTestStep) testStep;
            String stepText = pickleStep.getStep().getText();

            LogUtils.info("  Step Completed: " + stepText);
            LogUtils.info("    Status: " + status.name());
            LogUtils.info("    Duration: " + event.getResult().getDuration().toMillis() + " ms");

            if (!ConfigManager.isExtentReportEnabled()) {
                return;
            }

            // Log step result to ExtentReport
            try {
                if (ExtentTestManager.getTest() != null) {
                    if (status == io.cucumber.plugin.event.Status.PASSED) {
                        ExtentTestManager.getTest().pass("Step passed: " + stepText);
                    } else if (status == io.cucumber.plugin.event.Status.FAILED) {
                        Throwable error = event.getResult().getError();
                        String errorMsg = error != null ? error.getMessage() : "Unknown error";

                        // Capture screenshot for failed step
                        try {
                            if (DriverManager.getDriver() != null) {
                                TakesScreenshot ts = (TakesScreenshot) DriverManager.getDriver();
                                byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);

                                String sanitized = stepText.replaceAll("[^a-zA-Z0-9]", "_");
                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
                                String timestamp = LocalDateTime.now().format(dtf);
                                String screenshotName = sanitized + "_FAIL_" + timestamp + ".png";

                                // Absolute path untuk save file
                                String screenshotPath = ConfigManager.getScreenshotPath() + screenshotName;

                                File directory = new File(ConfigManager.getScreenshotPath());
                                if (!directory.exists()) {
                                    directory.mkdirs();
                                }

                                Files.write(Paths.get(screenshotPath), screenshot);
                                LogUtils.info("    Screenshot saved to: " + screenshotPath);

                                String relativeScreenshotPath = "../screenshots/" + screenshotName;

                                // Log to ExtentReport with screenshot at the failed step
                                ExtentTestManager.getTest().fail("Step failed: " + stepText)
                                        .fail(errorMsg)
                                        .addScreenCaptureFromPath(relativeScreenshotPath);
                            } else {
                                ExtentTestManager.getTest().fail("Step failed: " + stepText)
                                        .fail(errorMsg);
                            }
                        } catch (Exception screenshotEx) {
                            LogUtils.error("    Failed to capture screenshot: " + screenshotEx.getMessage());
                            ExtentTestManager.getTest().fail("Step failed: " + stepText)
                                    .fail(errorMsg);
                        }

                        LogUtils.error("    Error: " + errorMsg);
                    } else if (status == io.cucumber.plugin.event.Status.SKIPPED) {
                        ExtentTestManager.getTest().skip("Step skipped: " + stepText);
                    }
                }
            } catch (Exception e) {
                LogUtils.error("Failed to log step finish to ExtentReport: " + e.getMessage());
            }
        }
    }
}
