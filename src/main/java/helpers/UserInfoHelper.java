package helpers;

import managers.ConfigManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class UserInfoHelper {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");


    public static String getCurrentUserEmail() {
        return ConfigManager.getValidLoginEmail();
    }


    public static String getCurrentEnvironment() {
        return ConfigManager.getEnvironment();
    }

    public static String getUserAccountType() {
        String env = ConfigManager.getEnvironment().toLowerCase();
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
        return String.format(
                "Email: %s | Account: %s | Env: %s",
                getCurrentUserEmail(),
                getUserAccountType(),
                getCurrentEnvironment()
        );
    }


    public static String getDetailedUserInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("User Email: ").append(getCurrentUserEmail()).append("\n");
        sb.append("Account Type: ").append(getUserAccountType()).append("\n");
        sb.append("Environment: ").append(getCurrentEnvironment()).append("\n");
        sb.append("Test Data Source: env/").append(getCurrentEnvironment()).append(".properties\n");
        sb.append("Credentials Loaded At: ").append(LocalDateTime.now().format(DATE_FORMAT));
        return sb.toString();
    }

    public static String getUserTestHeader() {
        return String.format(
                "\n%s\n" +
                        "Testing as: %s (%s)\n" +
                        "Environment: %s\n" +
                        "%s\n",
                "=".repeat(50),
                getCurrentUserEmail(),
                getUserAccountType(),
                getCurrentEnvironment(),
                "=".repeat(50)
        );
    }
}
