package managers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigManager - Manages configuration with framework vs project separation
 *
 * Load Order (later = higher priority):
 *   1. framework/config.properties - Framework defaults (NEVER MODIFIED per project)
 *   2. projects/{project}/config.properties - Project-specific overrides
 *   3. projects/{project}/env/{env}.properties - Environment-specific config
 *
 * System Properties:
 *   -Dproject={name} - Select project (default: "default")
 *   -Denv={name} - Select environment (default: "dev")
 */
public class ConfigManager {
    private static Properties config;
    private static Properties frameworkConfig;
    private static Properties projectConfig;

    private static String environment;
    private static String project;
    private static final String DEFAULT_PROJECT = "EventHub"; // change this if want to change project by using this framework
    private static final String DEFAULT_ENV = "dev";
    private static final String DEFAULT_TEST_SUITE = "Regression Suite";

    // New path structure
    private static final String FRAMEWORK_CONFIG_PATH = "src/main/resources/framework/config.properties";
    private static final String PROJECTS_PATH = "src/main/resources/projects/";
    private static final String PROJECT_CONFIG_SUFFIX = "/config.properties";
    private static final String ENV_CONFIG_SUFFIX = "/env/";

    static {
        loadConfiguration();
    }

    private ConfigManager() {
        // Private constructor to prevent instantiation
    }

    private static void loadConfiguration() {
        try {
            // Get project and environment from system properties
            project = System.getProperty("project", DEFAULT_PROJECT).toLowerCase();
            environment = System.getProperty("env", DEFAULT_ENV).toLowerCase();

            System.out.println("Loading configuration for project: " + project + ", environment: " + environment);

            // 1. Load framework defaults (lowest priority)
            frameworkConfig = new Properties();
            try (InputStream frameworkInput = new FileInputStream(FRAMEWORK_CONFIG_PATH)) {
                frameworkConfig.load(frameworkInput);
                System.out.println("Framework configuration loaded from: " + FRAMEWORK_CONFIG_PATH);
            }

            // 2. Load project-specific config (overrides framework)
            projectConfig = new Properties();
            String projectConfigFile = PROJECTS_PATH + project + PROJECT_CONFIG_SUFFIX;
            try (InputStream projectInput = new FileInputStream(projectConfigFile)) {
                projectConfig.load(projectInput);
                System.out.println("Project configuration loaded from: " + projectConfigFile);
            }

            // 3. Load environment-specific config (highest priority)
            config = new Properties(frameworkConfig);
            config.putAll(projectConfig);

            String envConfigFile = PROJECTS_PATH + project + ENV_CONFIG_SUFFIX + environment + ".properties";
            try (InputStream envInput = new FileInputStream(envConfigFile)) {
                config.load(envInput);
                System.out.println("Environment configuration loaded from: " + envConfigFile);
            }

            System.out.println("Configuration loaded successfully for project: " + project + ", environment: " + environment);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration for project: " + project + ", environment: " + environment, e);
        }
    }

    // Get current project name
    public static String getProject() {
        return project;
    }

    // Get current environment name
    public static String getEnvironment() {
        return environment;
    }

    // Get base URL for the application
    public static String getBaseUrl() {
        return getProperty("BASE_URL");
    }

    // Get browser name
    public static String getBrowser() {
        return getProperty("BROWSER", "chrome");
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("HEADLESS", "false"));
    }

    // Get platform type (WEB or MOBILE)
    public static String getPlatform() {
        return getProperty("PLATFORM", "WEB");
    }

    // Get explicit wait timeout in seconds
    public static int getExplicitWaitTimeout() {
        return Integer.parseInt(getProperty("EXPLICIT_WAIT_TIMEOUT", "10"));
    }

    // Get hard wait timeout in seconds
    public static int getHardWaitTimeout() {
        return Integer.parseInt(getProperty("HARD_WAIT_TIMEOUT", "2"));
    }

    // Get step time in seconds
    public static int getStepTime() {
        return Integer.parseInt(getProperty("STEP_TIME", "0"));
    }

    // Get page load timeout in seconds
    public static int getPageLoadTimeout() {
        return Integer.parseInt(getProperty("PAGE_LOAD_TIMEOUT", "60"));
    }

    // Get screenshot path
    public static String getScreenshotPath() {
        return getProperty("SCREENSHOT_PATH", "./exports/ExportData/Images/");
    }

    // Get video recording path
    public static String getRecordVideoPath() {
        return getProperty("RECORD_VIDEO_PATH", "./exports/ExportData/Videos/");
    }

    public static boolean isRecordVideoEnabled() {
        return "yes".equalsIgnoreCase(getProperty("RECORD_VIDEO", "no"));
    }

    public static boolean isScreenshotStepAllEnabled() {
        return "yes".equalsIgnoreCase(getProperty("SCREENSHOT_STEP_ALL", "no"));
    }

    public static String getExtentReportPath() {
        return getProperty("EXTENT_REPORT_PATH", "exports/ExtentReport/ExtentReport.html");
    }

    public static String getExtentReportZipPath() {
        return getProperty("EXTENT_REPORT_ZIP_PATH", "exports/ExtentReport.zip");
    }

    public static String getAllureReportUrl() {
        String configuredUrl = getProperty("ALLURE_REPORT_URL", "");
        if (isNotBlank(configuredUrl)) {
            return configuredUrl.trim();
        }

        String buildUrl = System.getenv("BUILD_URL");
        if (isNotBlank(buildUrl)) {
            return appendPath(buildUrl, "allure");
        }

        return "";
    }

    public static boolean isAllureReportEnabled() {
        return getBooleanProperty("REPORT.ALLURE.ENABLED", true);
    }

    public static boolean isExtentReportEnabled() {
        return getBooleanProperty("REPORT.EXTENT.ENABLED", false);
    }

    public static boolean isEmailReportEnabled() {
        return getBooleanProperty("REPORT.EMAIL.ENABLED", getBooleanProperty("SEND_EMAIL_TO_USERS", false));
    }

    public static String getEmailSmtpHost() {
        return getProperty("EMAIL.SMTP.HOST", "smtp.gmail.com");
    }

    public static String getEmailSmtpPort() {
        return getProperty("EMAIL.SMTP.PORT", "587");
    }

    public static String getEmailFrom() {
        return getProperty("EMAIL.FROM", "");
    }

    public static String getEmailPassword() {
        return getProperty("EMAIL.PASSWORD", "");
    }

    public static String[] getEmailTo() {
        String to = getProperty("EMAIL.TO", "");
        if (!isNotBlank(to)) {
            return new String[0];
        }
        return java.util.Arrays.stream(to.split(","))
                .map(String::trim)
                .filter(ConfigManager::isNotBlank)
                .toArray(String[]::new);
    }

    public static String getReportTitle() {
        String title = getProperty("REPORT_TITLE", "Automation Test Execution Report - {env} - {suite}");
        String suiteName = getTestSuiteName();
        String envName = getEnvironment().toUpperCase();
        return title
                .replace("{env}", envName)
                .replace("{suite}", suiteName)
                .replace("${env}", envName)
                .replace("${suite}", suiteName);
    }

    public static String getTestSuiteName() {
        String suiteName = getProperty("TEST_SUITE", DEFAULT_TEST_SUITE);
        return isNotBlank(suiteName) ? suiteName.trim() : DEFAULT_TEST_SUITE;
    }

    public static String getTestSuiteTag() {
        String configuredTag = getProperty("TEST_SUITE_TAG", "");
        if (isNotBlank(configuredTag)) {
            return normalizeCucumberTag(configuredTag);
        }

        String suiteKey = normalizeSuiteKey(getTestSuiteName());
        String mappedTag = getProperty("TEST_SUITE_TAG." + suiteKey, "");
        if (isNotBlank(mappedTag)) {
            return normalizeCucumberTag(mappedTag);
        }

        return switch (suiteKey) {
            case "SIT" -> "@sit";
            case "STAGING" -> "@staging";
            case "SANITY" -> "@sanity";
            case "UAT" -> "@uat";
            case "PRODUCTION" -> "@production";
            default -> "@regression";
        };
    }

    public static void configureCucumberTagsForRunner(String runnerTag) {
        String normalizedRunnerTag = normalizeCucumberTag(runnerTag);
        String tagExpression = normalizedRunnerTag + " and " + getTestSuiteTag();
        System.setProperty("cucumber.filter.tags", tagExpression);
        System.out.println("Cucumber tag filter applied: " + tagExpression);
    }

    public static String getAuthor() {
        return getProperty("AUTHOR", "Automation Framework");
    }

    public static String getLocale() {
        return getProperty("LOCALE", getProperty("LOCATE", "en"));
    }

    public static String getValidLoginEmail() {
        return getProperty("VALID_LOGIN_EMAIL");
    }

    public static String getValidLoginPassword() {
        return getProperty("VALID_LOGIN_PASSWORD");
    }

    public static String getProperty(String key) {
        // Priority: System Property > Config Property > Environment Variable
        String systemValue = System.getProperty(key);
        if (systemValue != null) {
            return systemValue;
        }
        String envValue = System.getenv(key);
        if (envValue == null) {
            envValue = System.getenv(key.replace('.', '_'));
        }
        if (envValue != null) {
            return envValue;
        }
        return config.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }

    private static boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        return value != null ? Boolean.parseBoolean(value.trim()) : defaultValue;
    }

    public static int getRetryCount(){
        return Integer.parseInt(getProperty("RETRY_TEST_FAIL"));
    }

    private static boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private static String normalizeCucumberTag(String tag) {
        String normalizedTag = tag.trim();
        return normalizedTag.startsWith("@") ? normalizedTag : "@" + normalizedTag;
    }

    private static String normalizeSuiteKey(String suiteName) {
        String normalizedSuiteName = suiteName.trim().toUpperCase();
        if (normalizedSuiteName.endsWith(" SUITE")) {
            normalizedSuiteName = normalizedSuiteName.substring(0, normalizedSuiteName.length() - " SUITE".length());
        }
        return normalizedSuiteName.replaceAll("[^A-Z0-9]+", "_");
    }

    private static String appendPath(String baseUrl, String path) {
        if (baseUrl.endsWith("/")) {
            return baseUrl + path;
        }
        return baseUrl + "/" + path;
    }

    public static void reload() {
        loadConfiguration();
    }

    public static void printConfiguration() {
        System.out.println("========== Configuration Properties ==========");
        System.out.println("Project: " + project);
        System.out.println("Environment: " + environment);
        System.out.println("--------------------------------------------");
        System.out.println("Framework Config: " + FRAMEWORK_CONFIG_PATH);
        System.out.println("Project Config: " + PROJECTS_PATH + project + PROJECT_CONFIG_SUFFIX);
        System.out.println("Env Config: " + PROJECTS_PATH + project + ENV_CONFIG_SUFFIX + environment + ".properties");
        System.out.println("--------------------------------------------");
        config.forEach((key, value) -> System.out.println(key + " = " + value));
        System.out.println("=============================================");
    }

    public static final String ICON_OS_WINDOWS = "<i class='fa fa-windows' ></i>";
    public static final String ICON_OS_MAC = "<i class='fa fa-apple' ></i>";
    public static final String ICON_OS_LINUX = "<i class='fa fa-linux' ></i>";

    public static final String ICON_BROWSER_EDGE = "<i class=\"fa fa-edge\" aria-hidden=\"true\"></i>";
    public static final String ICON_BROWSER_CHROME = "<i class=\"fa fa-chrome\" aria-hidden=\"true\"></i>";
    public static final String ICON_BROWSER_FIREFOX = "<i class=\"fa fa-firefox\" aria-hidden=\"true\"></i>";

    // Override ExtentReport - if yes, report name will be ExtentReports.html (no timestamp)
    public static boolean isOverrideReportsEnabled() {
        return "yes".equalsIgnoreCase(getProperty("OVERRIDE_REPORTS", "no"));
    }
}
