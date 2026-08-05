package reports;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.InvalidPathForAllureReportFileException;
import factory.DriverManager;
import managers.ConfigManager;
import helpers.SystemHelper;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class AllureManager {
    private static final String ALLURE_RESULTS_DIRECTORY_PROPERTY = "allure.results.directory";
    private static final String DEFAULT_ALLURE_RESULTS_DIRECTORY = "exports/AllureReport";
    private static final List<String> ALLURE_CATEGORY_RESOURCES = List.of(
            "allure/categories/api-categories.json",
            "allure/categories/ui-categories.json"
    );
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    // Text attachment
    @Attachment(value = "{0}", type = "text/plain")
    public static String saveTextLog(String message) {
        if (!ConfigManager.isAllureReportEnabled()) {
            return null;
        }
        return message;
    }

    @Attachment(value = "{0}", type = "text/plain")
    public static String attachText(String attachmentName, String content) {
        if (!ConfigManager.isAllureReportEnabled()) {
            return null;
        }
        return content;
    }

    // HTML attachment
    @Attachment(value = "{0}", type = "text/html")
    public static String attachHtml(String html) {
        if (!ConfigManager.isAllureReportEnabled()) {
            return null;
        }
        return html;
    }

    //Text attachments for Allure
    @Attachment(value = "Page screenshot", type = "image/png")
    public static byte[] saveScreenshotPNG() {
        if (!ConfigManager.isAllureReportEnabled()) {
            return new byte[0];
        }
        return ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
    }

    // Executor metadata for Allure report Executors tab.
    public static void writeExecutorInfo() {
        if (!ConfigManager.isAllureReportEnabled()) {
            return;
        }

        try {
            Path resultsDirectory = getAllureResultsDirectory();
            Files.createDirectories(resultsDirectory);

            Map<String, Object> executor = new LinkedHashMap<>();
            executor.put("name", getExecutorName());
            executor.put("type", getExecutorType());
            executor.put("buildOrder", getBuildOrder());
            executor.put("buildName", getBuildName());
            executor.put("reportName", "Allure Report - " + valueOrNA(ConfigManager.getEnvironment()));

            OBJECT_MAPPER.writerWithDefaultPrettyPrinter()
                    .writeValue(resultsDirectory.resolve("executor.json").toFile(), executor);
        } catch (IOException e) {
            System.err.println("WARNING: Failed to write Allure executor info: " + e.getMessage());
        }
    }

    // Environment metadata for Allure report Environment tab.
    public static void writeEnvironmentInfo() {
        if (!ConfigManager.isAllureReportEnabled()) {
            return;
        }

        try {
            Path resultsDirectory = getAllureResultsDirectory();
            Files.createDirectories(resultsDirectory);

            Properties environment = new Properties();
            environment.setProperty("Environment", valueOrNA(ConfigManager.getEnvironment()));
            environment.setProperty("Maven.Profile.Env", valueOrNA(System.getProperty("env")));
            environment.setProperty("Base.URL", valueOrNA(ConfigManager.getBaseUrl()));
            environment.setProperty("Browser", valueOrNA(ConfigManager.getBrowser()));
            environment.setProperty("Headless.Mode", ConfigManager.isHeadless() ? "On" : "Off");
            environment.setProperty("Locale", valueOrNA(ConfigManager.getLocale()));
            environment.setProperty("Author", valueOrNA(ConfigManager.getAuthor()));
            environment.setProperty("Test.User.Email", valueOrNA(ConfigManager.getValidLoginEmail()));
            environment.setProperty("Account.Type", getAccountType());
            environment.setProperty("Operating.System", valueOrNA(SystemHelper.getOSName()));
            environment.setProperty("Java.Version", valueOrNA(System.getProperty("java.version")));
            environment.setProperty("Executor", getExecutorName());

            try (OutputStream output = Files.newOutputStream(resultsDirectory.resolve("environment.properties"))) {
                environment.store(output, "Allure environment");
            }
        } catch (IOException e) {
            System.err.println("WARNING: Failed to write Allure environment info: " + e.getMessage());
        }
    }

    public static void writeCategoriesInfo() {
        if (!ConfigManager.isAllureReportEnabled()) {
            return;
        }

        try {
            Path resultsDirectory = getAllureResultsDirectory();
            Files.createDirectories(resultsDirectory);

            List<Map<String, Object>> categories = new ArrayList<>();
            for (String resourcePath : ALLURE_CATEGORY_RESOURCES) {
                loadCategories(resourcePath, categories);
            }

            OBJECT_MAPPER.writerWithDefaultPrettyPrinter()
                    .writeValue(resultsDirectory.resolve("categories.json").toFile(), categories);
        } catch (IOException e) {
            System.err.println("WARNING: Failed to write Allure categories: " + e.getMessage());
        }
    }

    @Attachment(value = "Test Environment Information", type = "text/plain")
    public static String attachEnvironmentInfo() {
        if (!ConfigManager.isAllureReportEnabled()) {
            return null;
        }
        StringBuilder info = new StringBuilder();
        info.append("=== TEST ENVIRONMENT INFORMATION ===\n\n");

        String environment = valueOrNA(ConfigManager.getEnvironment());

        info.append("Environment: ").append(environment).append("\n");
        info.append("Base URL: ").append(valueOrNA(ConfigManager.getBaseUrl())).append("\n");
        info.append("Browser: ").append(valueOrNA(ConfigManager.getBrowser())).append("\n");
        info.append("Headless Mode: ").append(ConfigManager.isHeadless() ? "On" : "Off").append("\n");
        info.append("Locale: ").append(ConfigManager.getLocale()).append("\n");
        info.append("Execution Time: ").append(LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))).append("\n");
        return info.toString();
    }

    @Attachment(value = "Test User Account Information", type = "text/plain")
    public static String attachUserAccountInfo() {
        if (!ConfigManager.isAllureReportEnabled()) {
            return null;
        }
        StringBuilder userInfo = new StringBuilder();
        userInfo.append("=== TEST USER ACCOUNT INFORMATION ===\n\n");

        String email = valueOrNA(ConfigManager.getValidLoginEmail());
        String environment = valueOrNA(ConfigManager.getEnvironment());

        userInfo.append("Email: ").append(email).append("\n");
        userInfo.append("Account Type: ").append(getAccountType()).append("\n");
        userInfo.append("Environment: ").append(environment).append("\n");
        userInfo.append("Test Data Source: Environment Properties\n");
        userInfo.append("Credentials Loaded From: env/").append(environment).append(".properties\n");
        return userInfo.toString();
    }

    @Attachment(value = "System Configuration", type = "text/plain")
    public static String attachSystemConfig() {
        if (!ConfigManager.isAllureReportEnabled()) {
            return null;
        }
        StringBuilder config = new StringBuilder();
        config.append("=== SYSTEM CONFIGURATION ===\n\n");
        config.append("Operating System: ").append(SystemHelper.getOSName()).append("\n");
        config.append("Java Version: ").append(System.getProperty("java.version")).append("\n");
        config.append("Browser: ").append(ConfigManager.getBrowser()).append("\n");
        config.append("Author: ").append(ConfigManager.getAuthor()).append("\n");
        config.append("Explicit Wait Timeout: ").append(ConfigManager.getExplicitWaitTimeout()).append(" seconds\n");
        config.append("Page Load Timeout: ").append(ConfigManager.getPageLoadTimeout()).append(" seconds\n");
        config.append("Hard Wait Timeout: ").append(ConfigManager.getHardWaitTimeout()).append(" seconds\n");
        config.append("Step Time: ").append(ConfigManager.getStepTime()).append(" seconds\n");
        config.append("Screenshot Path: ").append(ConfigManager.getScreenshotPath()).append("\n");
        config.append("Record Video Path: ").append(ConfigManager.getRecordVideoPath()).append("\n");
        config.append("Record Video Enabled: ").append(ConfigManager.isRecordVideoEnabled()).append("\n");
        config.append("Screenshot Step All Enabled: ").append(ConfigManager.isScreenshotStepAllEnabled()).append("\n");
        return config.toString();
    }

    @Attachment(value = "Complete Test Context Information", type = "text/plain")
    public static String attachCompleteTestContext() {
        if (!ConfigManager.isAllureReportEnabled()) {
            return null;
        }
        StringBuilder context = new StringBuilder();
        context.append("=== COMPLETE TEST CONTEXT ===\n\n");
        context.append(attachEnvironmentInfo()).append("\n");
        context.append(attachUserAccountInfo()).append("\n");
        context.append(attachSystemConfig());
        return context.toString();
    }

    @Attachment(value = "Test Result Summary", type = "text/plain")
    public static String attachTestResultSummary(String testName, String status) {
        if (!ConfigManager.isAllureReportEnabled()) {
            return null;
        }
        StringBuilder summary = new StringBuilder();
        summary.append("=== TEST RESULT SUMMARY ===\n\n");
        summary.append("Test Name: ").append(testName).append("\n");
        summary.append("Status: ").append(status).append("\n");

        String email = valueOrNA(ConfigManager.getValidLoginEmail());
        String environment = valueOrNA(ConfigManager.getEnvironment());

        summary.append("Executed By: ").append(email).append("\n");
        summary.append("Environment: ").append(environment).append("\n");
        summary.append("Execution Date: ").append(LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))).append("\n");
        summary.append("Browser: ").append(valueOrNA(ConfigManager.getBrowser())).append("\n");
        return summary.toString();
    }

    // helper method
    private static String getAccountType() {
        String env = ConfigManager.getEnvironment();
        if (env == null) {
            System.err.println("WARNING: ConfigManager environment is NULL!");
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

    private static String valueOrNA(String value) {
        return value == null || value.isBlank() ? "N/A" : value;
    }

    private static Path getAllureResultsDirectory() {
        String directory = System.getProperty(ALLURE_RESULTS_DIRECTORY_PROPERTY, DEFAULT_ALLURE_RESULTS_DIRECTORY);
        Path path = Path.of(directory);

        // Validate path
        if (path.isAbsolute() && !path.toFile().exists()) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new InvalidPathForAllureReportFileException(
                    "Failed to create Allure results directory: " + directory, e);
            }
        }

        return path;
    }

    private static void loadCategories(String resourcePath, List<Map<String, Object>> categories) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                System.err.println("WARNING: Allure category resource not found: " + resourcePath);
                return;
            }

            categories.addAll(OBJECT_MAPPER.readValue(inputStream, new TypeReference<>() {
            }));
        }
    }

    private static String getExecutorName() {
        return "Local Machine";
    }

    private static String getExecutorType() {
        return "local";
    }

    private static long getBuildOrder() {
        return System.currentTimeMillis() / 1000;
    }

    private static String getBuildName() {
        String environment = valueOrNA(ConfigManager.getEnvironment());
        return environment + " - " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    private static String getBuildUrl() {
        return null;
    }
}
