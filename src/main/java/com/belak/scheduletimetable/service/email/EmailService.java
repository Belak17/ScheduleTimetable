package com.belak.scheduletimetable.service.email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String link) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(
                "Clique sur ce lien pour réinitialiser ton mot de passe :\n" + link +
                        "\nCe lien expire dans 15 minutes."
        );

        mailSender.send(message);
    }
}
