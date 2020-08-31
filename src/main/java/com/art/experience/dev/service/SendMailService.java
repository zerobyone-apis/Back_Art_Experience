package com.art.experience.dev.service;

import com.art.experience.dev.Configuration.MailPropertiesConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class SendMailService {

    private static final Logger LOGGER = LogManager.getLogger(SendMailService.class);

    private final Session mailSession;
    private final MailPropertiesConfig mailProperties;
    private final String subject;
    private final String description;

    @Autowired
    public SendMailService(final MailPropertiesConfig mailProperties,
                           final Session mailSession,
                           @Value("${mail.content.subject}") final String subject,
                           @Value("${mail.content.description}") final String description) {
        this.mailProperties = mailProperties;
        this.mailSession = mailSession;
        this.subject = subject;
        this.description = description;
    }

    public void notifyAndSendEmail(final String detailsEmails,
                                   final String username,
                                   final String dateTimesReserve,
                                   final String clientEmail) {
        LOGGER.info("STARTING SEND EMAIL PROCESS (> 0 _ 0 )> . . .  ");
        Optional<MimeMessage> mailContent =
                buildContentMail(detailsEmails, description, subject, username, dateTimesReserve, clientEmail);
        mailContent.ifPresent(this::connectAndSendEmail);
    }

    private Optional<MimeMessage> buildContentMail(final String detailsEmails,
                                                   final String description,
                                                   final String subject,
                                                   final String username,
                                                   final String dateTimesReserve,
                                                   final String clientEmail) {
        try {
            LOGGER.info("BUILDING EMAIL CONTENT BY MULTIPARTS BODY ( W_W )? . . .  ");
            MimeMessage mailContent = buildContentTextMail(subject, detailsEmails, clientEmail);

            // Se crea la part que contendra el texto de las reserva con la descripcion.
            // Podria ir perfectamente el detalle de la reserva.
            LOGGER.info("CREATING MULTIPART OF EMAIL TEXT");
            MimeBodyPart partOfContentText =
                    getContentPartAddTheText(detailsEmails, description, username, dateTimesReserve);


            // TODO:Aca se crea la el archivo a enviar en caso de enviar uno.
            //  -> MimeBodyPart partOfContentFile = getContentPartAddTheFile();
            //LOGGER.info("CREATING MULTIPART OF EMAIL TEXT");
            //DataSource swapSource = new ByteArrayDataSource(csvFile, "text/csv");
            //partOfContentFile.setDataHandler(new DataHandler(swapSource));

            LOGGER.info("ADDING THE MULTIPART TO THE BODY EMAIL . . .");
            Multipart multipartOfMail = new MimeMultipart();
            LOGGER.info("Multipart: CONTENT TEXT");
            multipartOfMail.addBodyPart(partOfContentText);
            //LOGGER.info("Multipart: CONTENT FILE");
            //multipartOfMail.addBodyPart(partOfContentFile);

            mailContent.setContent(multipartOfMail);
            return Optional.of(mailContent);
        } catch (MessagingException ex) {
            LOGGER.error("ERROR SENDING EMAIL INDEX OF ERROR: " + ex.getMessage());
        }
        return Optional.empty();
    }

    private void connectAndSendEmail(final MimeMessage mailContent) {
        try {
            LOGGER.info("SENDING EMAIL (>-e_-e)>  . . .  ");
            Transport accountAction = mailSession.getTransport("smtp");
            accountAction.connect(mailProperties.getHost(), mailProperties.getUsername(), mailProperties.getPassword());
            accountAction.sendMessage(mailContent, mailContent.getAllRecipients());
            accountAction.close();
            LOGGER.info("EMAIL SENDED SUCCESSFULLY!! \\( ^ - ^ )/  ");
        } catch (MessagingException me) {
            LOGGER.error("ERROR SENDING EMAIL: " + me.getMessage());
        }
    }

    private MimeMessage buildContentTextMail(final String subject,
                                             final String detailsEmail,
                                             final String clientEmail) throws MessagingException {
        LOGGER.info("CREATING CONTENT EMAIL . . . ");
        MimeMessage mailContent = new MimeMessage(mailSession);
        mailContent.setSubject(subject);
        //mailContent.setText(detailsEmail);
        setListOfAddresses(mailContent, clientEmail);
        return mailContent;
    }

    private MimeBodyPart getContentPartAddTheFile() throws MessagingException {
        LOGGER.info("CREATING CONTENT FILE FOR EMAIL . . . ");
        MimeBodyPart partOfContentFile = new MimeBodyPart();

        partOfContentFile.setFileName("TEST-REPORT.csv");
        return partOfContentFile;
    }

    private MimeBodyPart getContentPartAddTheText(final String detailsEmail,
                                                  final String description,
                                                  final String username,
                                                  final String dateTimeReserve) {
        MimeBodyPart partOfContentText = new MimeBodyPart();
        String htmlTemplate = getReservesFromJson(HTML_TEMPLATE_EMAIL_RESERVE_CONFIRM)
                .replace("[USERNAME]", username)
                .replace("[DETALLES]", detailsEmail)
                .replace("[DATETIME_RESERVE]", dateTimeReserve);

        try {
            partOfContentText.setContent(htmlTemplate, "text/html");
        } catch (MessagingException e) {
            LOGGER.error("ERROR FROM ADDRESS EXCEPTION INDEX OF ERROR: " + e.getMessage());
        }
        return partOfContentText;
    }

    private void setListOfAddresses(final MimeMessage mailContent, final String clientEmail) {
        try {
            // List of Addresses
            ArrayList<String> addresses = new ArrayList<>();
            LOGGER.info("Address added -> " + clientEmail);
            addresses.add(clientEmail);
            for (String address : mailProperties.getTo()) {
                LOGGER.info("Address added -> " + address);
                addresses.add(address);
            }

            mailContent.setFrom(new InternetAddress(mailProperties.getUsername()));
            InternetAddress[] listAddresses = new InternetAddress[mailProperties.getTo().length];
            for (int i = 0; i < mailProperties.getTo().length; i++) {
                listAddresses[i] = new InternetAddress(mailProperties.getTo()[i]);
                LOGGER.info("Adding mail To sent -> " + mailProperties.getTo()[i]);
            }
            for (int i = 0; i < listAddresses.length; i++) {
                mailContent.setRecipient(Message.RecipientType.TO, listAddresses[i]);
                LOGGER.info("Adding recipient To sent -> " + mailProperties.getTo()[i]);
            }
        } catch (AddressException ex) {
            LOGGER.error("ERROR FROM ADDRESS EXCEPTION INDEX OF ERROR: " + ex.getMessage());
        } catch (MessagingException ex) {
            LOGGER.error("ERROR SENDING EMAIL INDEX OF ERROR: " + ex.getMessage());
        }
    }

    private String getReservesFromJson(final String src) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(src)));
            return content;
        } catch (IOException e) {
            LOGGER.error("Something went wrong -> " + e.getMessage());
        }
        return content;
    }

    private final String HTML_TEMPLATE_EMAIL_RESERVE_CONFIRM = "src/main/resources/emailTemplates/EmailTemplateReservesConfirm.html";
}