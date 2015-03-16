package mooboxraspi;

/**
 * Created by mpacini on 13/03/15.
 */

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import javax.mail.Message;


public class ServiceMail {

    private Properties emailProperties;
    private Session mailSession;
    private MimeMessage emailMessage;

    public static void sendMail(String[] recipients, String subject, String message) throws AddressException,
            MessagingException {

        ServiceMail javaEmail = new ServiceMail();

        javaEmail.setMailServerProperties();
        javaEmail.createEmailMessage(recipients, subject, message);
        javaEmail.sendEmail();
    }

    private void setMailServerProperties() {

        String emailPort = "587";//gmail's smtp port

        emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", emailPort);
        emailProperties.put("mail.smtp.auth", "true");
        emailProperties.put("mail.smtp.starttls.enable", "true");

    }

    private void createEmailMessage(String[] recipients, String subject, String message) throws AddressException,
            MessagingException {
        String[] toEmails = recipients;
        String emailSubject = subject;
        String emailBody = message;

        mailSession = Session.getDefaultInstance(emailProperties, null);
        emailMessage = new MimeMessage(mailSession);

        for (int i = 0; i < toEmails.length; i++) {
            emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmails[i]));
        }

        emailMessage.setSubject(emailSubject);
        emailMessage.setContent(emailBody, "text/html");//for a html email
        //emailMessage.setText(emailBody);// for a text email

    }

    private void sendEmail() throws AddressException, MessagingException {

        String emailHost = "smtp.gmail.com";
        String fromUser = "...";//just the id alone without @gmail.com
        String fromUserEmailPassword = "...";

        Transport transport = mailSession.getTransport("smtp");

        transport.connect(emailHost, fromUser, fromUserEmailPassword);
        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
        transport.close();
        System.out.println("Email sent successfully.");
    }
}
