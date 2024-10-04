package by.it_academy.jd2.golubev_107.mail_sender.service.impl;

import by.it_academy.jd2.golubev_107.mail_sender.service.IMailSender;
import by.it_academy.jd2.golubev_107.mail_sender.service.config.MailSenderConfig;
import by.it_academy.jd2.golubev_107.mail_sender.service.dto.EmailOutDto;
import by.it_academy.jd2.golubev_107.mail_sender.storage.entity.Recipient;
import jakarta.mail.AuthenticationFailedException;
import jakarta.mail.Authenticator;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.util.List;

public class MailSender implements IMailSender {

    public static final int MIN_RECIPIENT_COUNT = 1;
    private final MailSenderConfig config;

    public MailSender(MailSenderConfig config) {
        this.config = config;
    }

    @Override
    public void send(EmailOutDto email) {
        Session mailSession = Session.getInstance(config.getMailProps(),
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(config.getUser(), config.getPassword());
                    }
                });

        mailSession.setDebug(config.isDebugModeOn());
        try {
            Message message = new MimeMessage(mailSession);

            message.setFrom(new InternetAddress(config.getFrom()));
            setMessageRecipients(message, Message.RecipientType.TO, email.getRecipientsTo());
            setMessageRecipients(message, Message.RecipientType.CC, email.getRecipientsCC());
            setMessageRecipients(message, Message.RecipientType.BCC, email.getRecipientsBCC());

            message.setSubject(email.getTitle());

            BodyPart mailBody = new MimeBodyPart();
            mailBody.setContent(email.getText(), "text/html; charset=utf-8");

            Multipart multiPart = new MimeMultipart();
            multiPart.addBodyPart(mailBody);

            message.setContent(multiPart);

            Transport.send(message);
        } catch (AddressException e) {
            throw new RuntimeException("Incorrect address" + e);
        } catch (AuthenticationFailedException e) {
            throw new RuntimeException("Incorrect login or password" + e);
        } catch (MessagingException e) {
            throw new RuntimeException("Something went wrong: " + e);
        }
    }

    private void setMessageRecipients(Message message, Message.RecipientType type,
                                      List<Recipient> recipients) throws MessagingException {
        if (recipients.isEmpty()) {
            return;
        }
        String recipientsAsString = getRecipientsAsString(recipients);
        if (recipients.size() > MIN_RECIPIENT_COUNT) {
            message.setRecipients(type, InternetAddress.parse(recipientsAsString));
        } else {
            message.addRecipient(type, new InternetAddress(recipientsAsString));
        }
    }

    private String getRecipientsAsString(List<Recipient> recipients) {
        List<String> addresses = recipients.stream()
                                           .map(e -> e.getAddress().getEmailAddress())
                                           .toList();
        return String.join(",", addresses);
    }
}
