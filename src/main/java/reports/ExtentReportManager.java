package reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import managers.ConfigManager;
import helpers.SystemHelper;
import utils.IconUtils;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExtentReportManager {
    private static ExtentReports extentReports = null;

    public synchronized static ExtentReports getExtentReports() {
        if (!ConfigManager.isExtentReportEnabled()) {
            return null;
        }

        if (extentReports == null) {
            try{
                // Generate timestamp for unique report filename
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
                String timestamp = LocalDateTime.now().format(dtf);

                // Get absolute path
                String basePath = System.getProperty("user.dir");

                // Build report filename based on OVERRIDE_REPORTS setting
                String reportFileName;
                if (ConfigManager.isOverrideReportsEnabled()) {
                    // If OVERRIDE_REPORTS=yes -> Report name will be ExtentReports.html
                    reportFileName = "ExtentReports.html";
                } else {
                    // If OVERRIDE_REPORTS=no -> Report name will be ExtentReport_timestamp.html
                    reportFileName = "ExtentReport_" + timestamp + ".html";
                }

                String relativePath = ConfigManager.getExtentReportPath().replace("ExtentReport.html", reportFileName);

                // Create absolute path
                String reportPath = basePath + File.separator + relativePath;

                // Create directory if not exists
                File reportFile = new File(reportPath);
                File reportDir = reportFile.getParentFile();
                if (!reportDir.exists()) {
                    boolean created = reportDir.mkdirs();
                    System.out.println("Directory created: " + created + " at " + reportDir.getAbsolutePath());
                }

                extentReports = new ExtentReports();
                ExtentSparkReporter reporter = new ExtentSparkReporter(reportPath);
                reporter.config().setReportName("Extent Report for Automation Test");
                reporter.config().setDocumentTitle("Test Automation Report");

                // Added reporter attachment
                extentReports.attachReporter(reporter);

                //  Added Null Check
                extentReports.setSystemInfo("Framework Name", "Cucumber Selenium Java");
                extentReports.setSystemInfo("Author", valueOrNA(ConfigManager.getAuthor()));
                extentReports.setSystemInfo("Browser", IconUtils.getBrowserIcon() + " " + (ConfigManager.getBrowser() != null ? ConfigManager.getBrowser() : "N/A"));

                // Use environment from ConfigManager
                String environment = valueOrNA(ConfigManager.getEnvironment());
                extentReports.setSystemInfo("Environment", environment);
                extentReports.setSystemInfo("Test Suite", ConfigManager.getTestSuiteName());
                extentReports.setSystemInfo("Version", "1.0");

                // User Information from ConfigManager
                String testUserEmail = valueOrNA(ConfigManager.getValidLoginEmail());
                extentReports.setSystemInfo("Test User Email", testUserEmail);
                extentReports.setSystemInfo("Test Account Type", getUserAccountType());
                extentReports.setSystemInfo("Test Data Source", "Environment Properties - env/" + environment + ".properties");

                // ENHANCEMENT: Execution Environment
                extentReports.setSystemInfo("Operating System", IconUtils.getOSIcon() + " " + SystemHelper.getOSName());
                extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
                extentReports.setSystemInfo("Report Generated", LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
                extentReports.setSystemInfo("Headless Mode", ConfigManager.isHeadless() ? "On" : "Off");
                extentReports.setSystemInfo("Page Load Timeout (seconds)", String.valueOf(ConfigManager.getPageLoadTimeout()));
                extentReports.setSystemInfo("Locale", ConfigManager.getLocale());

                System.out.println("ExtentReport initialized at: " + reportPath);
            }catch (Exception e){
                System.err.println("Failed to initialize ExtentReport: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return extentReports;
    }

    // ENHANCEMENT: Helper Methods for User Info
    private static String getUserAccountType() {
        // Use environment from ConfigManager
        String env = ConfigManager.getEnvironment();

        if (env == null) {
            System.err.println("WARNING: Environment is NULL! Check if properties are loaded correctly.");
            return "Unknown Account";
        }

        env = env.toLowerCase();
        if (env.contains("prod")) {
            return "Production Account";
        } else if (env.contains("staging")) {
            return "Staging Account";
        } else if (env.contains("dev")) {
            return "Development Account";
        }
        return "Test Account (" + env + ")";
    }

    public static String getFormattedUserInfo() {
        // Use info from ConfigManager
        String email = valueOrNA(ConfigManager.getValidLoginEmail());
        String environment = valueOrNA(ConfigManager.getEnvironment());

        return String.format(
                "User Info:\n" +
                        "  - Email: %s\n" +
                        "  - Account Type: %s\n" +
                        "  - Environment: %s\n" +
                        "  - Test Data Source: Environment Properties (env/%s.properties)",
                email,
                getUserAccountType(),
                environment,
                environment
        );
    }

    private static String valueOrNA(String value) {
        return value == null || value.isBlank() ? "N/A" : value;
    }
}
