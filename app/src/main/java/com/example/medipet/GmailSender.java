package com.example.medipet;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class GmailSender {
    private final String email;
    private final String password;

    public GmailSender(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void sendEmail(String toEmail, String subject, String messageBody) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email, password);
                    }
                });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(email));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject(subject);
        message.setText(messageBody);

        message.setContent(messageBody, "text/html; charset=utf-8");

        Transport.send(message);
    }
}
