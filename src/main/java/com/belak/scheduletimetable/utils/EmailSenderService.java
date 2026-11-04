package com.belak.scheduletimetable.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@AllArgsConstructor
@Service
@Slf4j
public class EmailSenderService implements EmailSender {
    private final static Logger LOGGER = Logger.getLogger(EmailSenderService.class.getName());
    private  final JavaMailSender mailSender;



    @Override
    @Async("emailExecutor")
    public void sendEmail(String to, String subject, String email) {

        try {

            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, false, "UTF-8");

            helper.setFrom("akabeb.com@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(email, true);

            mailSender.send(mimeMessage);

            log.info("Email envoyé avec succès à {}", to);

        } catch (Exception ex) {

            log.error("Erreur lors de l'envoi de l'email à {}", to, ex);
        }
    }
}
