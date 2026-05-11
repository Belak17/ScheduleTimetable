package com.belak.scheduletimetable.service.reset;

import com.belak.scheduletimetable.model.PasswordResetToken;
import com.belak.scheduletimetable.model.User;
import com.belak.scheduletimetable.repository.TokenRepository;
import com.belak.scheduletimetable.repository.UserRepository;
import com.belak.scheduletimetable.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ForgotPasswordService {
    private  final UserRepository userRepository ;
    private  final TokenRepository tokenRepository ;
    private  final EmailService emailService ;
    public void sendResetLink(String email)
    {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            String token = UUID.randomUUID().toString();

            PasswordResetToken resetToken = new PasswordResetToken();
            resetToken.setToken(token);
            resetToken.setUser(user.get());
            resetToken.setExpirationDate(LocalDateTime.now().plusMinutes(15));

            tokenRepository.save(resetToken);

            String link = "localhost:8080/reset-password?token=" + token;

            emailService.sendEmail(user.get().getEmail(), "Reset Password", link);
        }
    }
}
