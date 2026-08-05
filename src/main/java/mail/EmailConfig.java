package mail;

import managers.ConfigManager;

public class EmailConfig {
    public static final String SERVER = ConfigManager.getEmailSmtpHost();
    public static final String PORT = ConfigManager.getEmailSmtpPort();

    public static final String FROM = ConfigManager.getEmailFrom();
    public static final String PASSWORD = ConfigManager.getEmailPassword();

    public static final String[] TO = ConfigManager.getEmailTo();
    public static final String SUBJECT = ConfigManager.getReportTitle();
}
