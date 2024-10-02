package com.colour.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private static final String senderEmail = "colohrby@gmail.com";
    private static int authNumber;

    public static void createNumber() {
        authNumber = (int)(Math.random() * (97520)) + 100000;
    }

    public MimeMessage createMail(String mail) {
        MimeMessage message = mailSender.createMimeMessage();
        createNumber();
        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("Email Authentication");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + authNumber + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return message;
    }

    public int sendMail(String mail) {
        MimeMessage message = createMail(mail);
        mailSender.send(message);

        return authNumber;
    }
}
