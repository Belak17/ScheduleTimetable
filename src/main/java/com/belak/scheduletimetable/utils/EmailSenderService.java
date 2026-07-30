package com.belak.scheduletimetable.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@AllArgsConstructor
@Service
public class EmailSenderService implements EmailSender {
    private final static Logger LOGGER = Logger.getLogger(EmailSenderService.class.getName());
    private  final JavaMailSender mailSender;


    @Override
    @Async
    public void sendEmail(String to, String subject , String email) {

        try {
            //MimeMessage mimeMessage = mailSender.createMimeMessage();
            //MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true,"utf-8");
            //helper.setText(email,true);
            //helper.setTo(to);
            //helper.setSubject("Registration Confirmation");
            //helper.setFrom("Hello Kaleb");

            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom("akabeb.com@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(email);

            mailSender.send(message);
        }
        catch (Exception ex) {
            Logger.getLogger(EmailSenderService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
