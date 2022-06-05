package com.csproject.hrm.common.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailUtils {
  private static final String emailSubject = "Your password";

  public static void sendEmail(String emailToAddress, String textMessage) {

    final String email = "hihihd37@gmail.com";
    final String password = "hihihaha";

    Properties props = new Properties();

    props.put("mail.smtp.user", "username");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "25");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.EnableSSL.enable", "true");

    props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    props.setProperty("mail.smtp.socketFactory.fallback", "false");
    props.setProperty("mail.smtp.port", "465");
    props.setProperty("mail.smtp.socketFactory.port", "465");

    Session session =
        Session.getInstance(
            props,
            new Authenticator() {
              @Override
              protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
              }
            });

    try {
      Message message = new MimeMessage(session);

      message.setFrom(new InternetAddress(email));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailToAddress));
      message.setSubject(emailSubject);
      message.setContent(textMessage, "text/html; charset=UTF-8");
      // send the email
      Transport.send(message);

    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }
}
