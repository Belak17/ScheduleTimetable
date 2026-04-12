package com.belak.scheduletimetable.service.reset;

import com.belak.scheduletimetable.exception.ResourceNotFoundException;
import com.belak.scheduletimetable.model.PasswordResetToken;
import com.belak.scheduletimetable.model.User;
import com.belak.scheduletimetable.repository.TokenRepository;
import com.belak.scheduletimetable.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private  final TokenRepository tokenRepository ;
    private final UserRepository userRepository ;
    private  final PasswordEncoder passwordEncoder ;
    public void resetPassword(String token , String newPassword)
    {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);

        if (resetToken == null ||
                resetToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw  new ResourceNotFoundException("Token Expire");

        }
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        tokenRepository.delete(resetToken);

    }
}
