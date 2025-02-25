package tn.esprit.innoxpert.Util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailClass {
  private String username = "Innohire45@gmail.com";
  private String password = "rqau hpkp wtxu dvmg";

  public void envoyer(String reciever, String OTP) {
    // Etape 1 : Création de la session
    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.ssl.enable", "true");
    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    props.put("mail.smtp.socketFactory.fallback", "false");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "465");
    props.put("mail.smtp.ssl.protocols", "TLSv1.2");

    Session session = Session.getInstance(props,
      new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(username, password);
        }
      });

    try {
      // Etape 2 : Création de l'objet Message
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress("Innohire45@gmail.com"));
      message.setRecipients(Message.RecipientType.TO,
        InternetAddress.parse(reciever));
      message.setSubject("Innohire code");
      message.setText(OTP);

      // Etape 3 : Envoyer le message
      Transport.send(message);
      System.out.println("Message envoyé");
    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }

  public void sendConfirmationEmail(String receiver, String fullName, String dateDebut, String dateFin) {
    // New confirmation email sending logic
    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.ssl.enable", "true");
    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    props.put("mail.smtp.socketFactory.fallback", "false");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "465");
    props.put("mail.smtp.ssl.protocols", "TLSv1.2");

    Session session = Session.getInstance(props,
      new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(username, password);
        }
      });

    try {
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress("Innohire45@gmail.com"));
      message.setRecipients(Message.RecipientType.TO,
        InternetAddress.parse(receiver));
      message.setSubject("Congé Confirmé");
      message.setText("Bonjour " + fullName + ",\n\nVotre congé prévu pour la période du " + dateDebut + " au " + dateFin + " a été confirmé.\n\nCordialement,\nL'équipe RH");

      Transport.send(message);
      System.out.println("email de confirmation envoyé");
    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }

  public void sendRejectionEmail(String receiver, String fullName, String dateDebut, String dateFin) {
    // New rejection email sending logic
    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.ssl.enable", "true");
    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    props.put("mail.smtp.socketFactory.fallback", "false");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "465");
    props.put("mail.smtp.ssl.protocols", "TLSv1.2");

    Session session = Session.getInstance(props,
      new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(username, password);
        }
      });

    try {
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress("Innohire45@gmail.com"));
      message.setRecipients(Message.RecipientType.TO,
        InternetAddress.parse(receiver));
      message.setSubject("Congé Non Accepté");
      message.setText("Bonjour " + fullName + ",\n\nNous regrettons de vous informer que votre demande de congé pour la période du " + dateDebut + " au " + dateFin + " n'a pas été acceptée.\n\nCordialement,\nL'équipe RH");

      Transport.send(message);
      System.out.println("email de rejet envoyé");
    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }


  }
}
