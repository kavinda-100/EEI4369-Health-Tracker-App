package com.s22010170.heathtrakerapp.utils;


import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmails {
    private final String senderEmail;
    private final String passwordSenderEmail;
    private final String host;

    public SendEmails(String senderEmail, String passwordSenderEmail) {
        this.senderEmail = senderEmail;
        this.passwordSenderEmail = passwordSenderEmail;
        this.host = "smtp.gmail.com";
    }
    public boolean sendEmailToUser(String receiverEmail, String subject, String text) {
        try {
            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, passwordSenderEmail);
                }
            });


            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiverEmail));
            message.setSubject(subject);
            message.setText(text);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(message);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            });
            thread.start();
//            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    // method for getting the 6 digit OTP
    public String getOTP() {
        int OTP = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(OTP);
    }
}


//TODO: Reference Link
//https://programmerworld.co/android/how-to-send-email-using-gmail-smtp-server-directly-from-your-android-app/