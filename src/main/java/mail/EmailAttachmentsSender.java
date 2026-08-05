package mail;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.Properties;

public class EmailAttachmentsSender {
    private static final String DEFAULT_TIMEOUT_MS = "30000";

    public static void sendEmailWithAttachments(String host, String port, final String userName, final String password,
                                                String[] toAddress, String subject, String message, String... attachFiles)
            throws AddressException, MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.starttls.required", "true");
        properties.put("mail.smtp.connectiontimeout", getTimeout("EMAIL_SMTP_CONNECTION_TIMEOUT_MS"));
        properties.put("mail.smtp.timeout", getTimeout("EMAIL_SMTP_TIMEOUT_MS"));
        properties.put("mail.smtp.writetimeout", getTimeout("EMAIL_SMTP_WRITE_TIMEOUT_MS"));
        properties.put("mail.smtp.ssl.trust", host);
        properties.put("mail.user", userName);
        properties.put("mail.password", password);

        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(properties, auth);

        Message msg = new MimeMessage(session);
        InternetAddress from = new InternetAddress(userName, false);
        from.validate();
        msg.setFrom(from);

        InternetAddress[] addressTo = new InternetAddress[toAddress.length];
        for (int i = 0; i < toAddress.length; i++) {
            addressTo[i] = new InternetAddress(toAddress[i], false);
            addressTo[i].validate();
        }
        msg.setRecipients(Message.RecipientType.TO, addressTo);

        msg.setSubject(subject);
        msg.setSentDate(new Date());

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(message, "text/html; charset=UTF-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        if (attachFiles != null && attachFiles.length > 0) {
            for (String filePath : attachFiles) {
                if (filePath == null || filePath.isBlank()) {
                    continue;
                }
                Path attachment = Path.of(filePath);
                if (!Files.isRegularFile(attachment)) {
                    throw new MessagingException("Attachment file not found: " + attachment);
                }
                MimeBodyPart attachPart = new MimeBodyPart();
                attachFile(attachPart, attachment);
                multipart.addBodyPart(attachPart);
            }
        }

        msg.setContent(multipart);
        Transport.send(msg);
    }

    private static void attachFile(MimeBodyPart attachPart, Path attachment) throws MessagingException {
        try {
            attachPart.attachFile(attachment.toFile());
        } catch (IOException e) {
            throw new MessagingException("Failed to attach file: " + attachment, e);
        }
    }

    private static String getTimeout(String envName) {
        String value = System.getenv(envName);
        if (value == null || value.isBlank()) {
            return DEFAULT_TIMEOUT_MS;
        }
        return value;
    }
}
