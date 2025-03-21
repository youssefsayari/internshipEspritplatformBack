package tn.esprit.innoxpert.Util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailClass {

  private final String username = "esprit.stagedepartement@gmail.com";
  private final String password = "xmqu futu clki tskp";

  private Session createEmailSession() {
    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.ssl.enable", "true");
    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    props.put("mail.smtp.socketFactory.fallback", "false");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "465");
    props.put("mail.smtp.ssl.protocols", "TLSv1.2");

    return Session.getInstance(props, new jakarta.mail.Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
    });
  }

  public void sendMeetingReminder(String receiver, String organiserName, String participantName, String meetingDate, String meetingTime, String meetingLink) {
    try {
      Session session = createEmailSession();
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(username));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
      message.setSubject("🔔 Rappel de votre réunion - Jitsi Meet");

      String formattedMessage = String.format(
              "<html><body>"
                      + "<h2>📢 Rappel de réunion</h2>"
                      + "<p>Bonjour,</p>"
                      + "<p>Un rappel pour votre réunion prévue entre :</p>"
                      + "<ul>"
                      + "<li><b>📌 Organisateur :</b> %s</li>"
                      + "<li><b>👤 Participant :</b> %s</li>"
                      + "</ul>"
                      + "<p><b>📅 Date :</b> %s</p>"
                      + "<p><b>⏰ Heure :</b> %s</p>"
                      + "<p><b>🔗 Lien de la réunion :</b> <a href='%s'>Cliquez ici pour rejoindre</a></p>"
                      + "<p>Merci de vous connecter à l'heure prévue.</p>"
                      + "<p>Cordialement,<br>Votre équipe.</p>"
                      + "</body></html>",
              organiserName, participantName, meetingDate, meetingTime, meetingLink
      );

      message.setContent(formattedMessage, "text/html; charset=utf-8");
      Transport.send(message);
      System.out.println("✔️ Email de rappel envoyé à " + receiver);
    } catch (MessagingException e) {
      System.err.println("❌ Erreur d'envoi de l'email à " + receiver + " : " + e.getMessage());
    }
  }

  public void sendHtmlEmail(String receiver, String subject, String htmlContent) {
    try {
      Session session = createEmailSession();
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(username));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
      message.setSubject(subject);
      message.setContent(htmlContent, "text/html; charset=utf-8");

      Transport.send(message);
      System.out.println("✔️ Email HTML envoyé à " + receiver);
    } catch (MessagingException e) {
      System.err.println("❌ Erreur d'envoi de l'email à " + receiver + " : " + e.getMessage());
    }
  }

  public void sendOtpEmail(String receiver, Long otp) {
    try {
      Session session = createEmailSession();
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(username));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
      message.setSubject("🔐 Password Recovery OTP");

      String htmlContent = String.format(
              "<html><body>"
                      + "<h2>🔐 Password Recovery - OTP</h2>"
                      + "<p>Hello,</p>"
                      + "<p>You requested a password recovery for your account. Your OTP code is: <b>%d</b></p>"
                      + "<p>Please use this OTP to reset your password. The OTP is valid for a limited time.</p>"
                      + "<p>If you didn't request this change, please ignore this email.</p>"
                      + "<p>Regards,<br>Your Security Team</p>"
                      + "</body></html>",
              otp
      );

      message.setContent(htmlContent, "text/html; charset=utf-8");
      Transport.send(message);
      System.out.println("✔️ OTP email sent for password recovery to " + receiver);
    } catch (MessagingException e) {
      System.err.println("❌ Error sending OTP email for password recovery to " + receiver + " : " + e.getMessage());
    }
  }
}
