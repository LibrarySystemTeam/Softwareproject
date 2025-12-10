package library.communication;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

/**
 * Sends real email notifications using Gmail SMTP.
 * This class is part of Sprint 3 (Reminders + Real Email Support).
 */
public class EmailNotificationSender implements NotificationSender {

    /** Admin Gmail used as sender */
    private final String senderEmail = "batoolmasri408@gmail.com";

    /** Google App Password (NOT the Gmail password) */
    private final String senderPassword = System.getenv("EMAIL_APP_PASSWORD");
    private static final Logger logger = Logger.getLogger(EmailNotificationSender.class.getName());

    @Override
    public void send(String toEmail, String message) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(senderEmail));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            msg.setSubject("Library Notification");
            msg.setText(message);

            Transport.send(msg);
            System.out.println("Email Sent Successfully to: " + toEmail);

        } catch (Exception e) {
        	logger.severe("Failed to send email");

            e.printStackTrace();
        }
    }
}
