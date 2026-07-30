package com.belak.scheduletimetable.service.reset;

import com.belak.scheduletimetable.exception.ResourceNotFoundException;
import com.belak.scheduletimetable.model.ConfirmationToken;
import com.belak.scheduletimetable.model.PasswordResetToken;
import com.belak.scheduletimetable.model.ResetToken;
import com.belak.scheduletimetable.model.User;
import com.belak.scheduletimetable.repository.ResetPasswordTokenRepository;
import com.belak.scheduletimetable.repository.TokenRepository;
import com.belak.scheduletimetable.repository.UserRepository;
import com.belak.scheduletimetable.utils.EmailSender;
import com.belak.scheduletimetable.utils.EmailValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private  final TokenRepository tokenRepository ;
    private final UserRepository userRepository ;
    private  final PasswordEncoder passwordEncoder ;
    private  final EmailValidator emailValidator ;
    private final EmailSender emailSender ;
    private  final ResetPasswordTokenRepository resetPasswordTokenRepository ;

    public String  resetPassword(String email) {
        boolean isValidEmail = emailValidator
                .test(email);
        if (!isValidEmail)
        {
            throw new IllegalArgumentException("email not valid");
        }
        User theUser = userRepository.findByEmail(email).orElse(null);
        if (theUser == null)
        {
            throw new IllegalArgumentException("User not found");
        }
        String token = reset(
            theUser
        );

        String link = "http://localhost:8082/reset/password?token=" + token;
        emailSender.sendEmail(
                email,"Reset Password ",
                buildEmail(theUser.getPrenom(), link));
        return token;

    }

    public String reset(User appUser)
    {

        if (!appUser.isEnabled())
        {
            // TODO check of attributes are the same and
            // TODO if email not confirmed send confirmation email

            throw  new IllegalArgumentException("Account disabled . You can't reset your email");
        }
        String token = UUID.randomUUID().toString();
        // TODO : SEND CONFIRMATION TOKEN
        ResetToken resetToken = new ResetToken(
                token ,
                LocalDateTime.now() ,
                LocalDateTime.now().plusMinutes(15),
                appUser

        );
        resetPasswordTokenRepository.save(resetToken);
        // TODO : SEND EMAIL
        return  token ;
    }

    public  String buildEmail(String name, String link) {

        return "Hi " + name + "\n\n" +
                "Welcome back  \n\n" +
                "Please click the link below to reset your password :\n\n" +
                link + "\n\n" +
                "This link will expire in 15 minutes.\n\n" +
                "See you soon";
    }

    @Transactional
    public String confirmResetToken(String token , String password)
    {
        ResetToken resetToken = resetPasswordTokenRepository.findByToken(token)
                .orElseThrow(
                        () -> new IllegalStateException("token not found"));
        if (resetToken.getConfirmedAt()!=null)
        {
            throw new IllegalStateException("token already confirmed");
        }
        LocalDateTime expiredAt = resetToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now()))
        {
            throw new IllegalStateException("token expired");
        }
        resetToken.setConfirmedAt(LocalDateTime.now());
        resetUserPassword(resetToken.getAppUser(),password);
        return "User Password Changed";

    }
    public void resetUserPassword(User user , String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }
}
