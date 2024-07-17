package it.unisannio.ingsw24.gateway.presentation;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.util.Properties;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class EmailService {

    private final String username = "truckparkingapp@gmail.com";
    private final String password = "themzfgqwpavnilr";

    public void sendEmailWithAttachment(String to, byte[] attachmentData) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });


        try {
            String subject = "Prenotazione";
            String body = "Prenotazione affettuata con successo, QR-CODE allegato";
            String filename = "qr_code.png";
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            // Crea il corpo del messaggio
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);

            // Crea il multipart
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // Crea l'allegato
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.setFileName(filename);
            ByteArrayInputStream bais = new ByteArrayInputStream(attachmentData);
            attachmentPart.setDataHandler(new DataHandler(new ByteArrayDataSource(bais, "image/png")));
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

//            System.out.println("Invio del messaggio email con allegato...");
            Transport.send(message);
//            System.out.println("Email con allegato inviata con successo!");
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    public void sendEmailRegistration(String toEmail, String name) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {

            String subject = "Registration Successful";
            String body = "Benvenuto " + name + ",\n\nla tua registrazione è avvenuta con successo.";

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
