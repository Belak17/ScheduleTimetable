package com.belak.scheduletimetable.service.user;

import com.belak.scheduletimetable.model.ConfirmationToken;
import com.belak.scheduletimetable.model.User;
import com.belak.scheduletimetable.repository.UserRepository;
import com.belak.scheduletimetable.request.UserRegister;
import com.belak.scheduletimetable.service.ConfirmationTokenService;
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
public class UserRegisterService implements UserRegisterInterfaceService {
    private  final UserRepository userRepository ;
    private  final PasswordEncoder passwordEncoder ;
    private  final UserService userService ;
    private  final EmailValidator emailValidator ;
    private  final ConfirmationTokenService confirmationTokenService ;

    private final  static  String USER_NOT_FOUND_MSG =
            "user with email %s not found";

    private  final EmailSender emailSender ;
    public boolean existsUserByUserIdAndEmail(UserRegister userRegister)
    {
        return userRepository.existsByUserIdAndEmail(userRegister.getUserId(),userRegister.getEmail());
    }


    public void registerUserByUserIdAndEmail(UserRegister userRegister)
    {
        if (existsUserByUserIdAndEmail(userRegister))
        {
            User user = userRepository.findByEmail(userRegister.getEmail()).get();
            user.setEnabled(true) ;
            user.setPassword(passwordEncoder.encode(userRegister.getPassword()));
            userRepository.save(user);
        }
    }
    public  String register(UserRegister request)
    {
        boolean isValidEmail = emailValidator
                .test(request.getEmail());
        if (!isValidEmail)
        {
            throw new IllegalArgumentException("email not valid");
        }
        String token = signup(
                new User(
                        request.getUserId(),
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User theUser = userRepository.findByEmail(request.getEmail()).get();
        String link = "http://localhost:8082/confirm?token=" + token;
        emailSender.sendEmail(
                request.getEmail(),"Registration Confirmation",
                buildEmail(theUser.getPrenom(), link));
        return token;
    }

    public  String buildEmail(String name, String link) {

        return "Hi " + name + "\n\n" +
                "Thank you for registering.\n\n" +
                "Please click the link below to activate your account:\n\n" +
                link + "\n\n" +
                "This link will expire in 15 minutes.\n\n" +
                "See you soon";
    }

    public String signup(User appUser)
    {
        User theUserExists = userRepository.findByEmail(appUser.getEmail()).get() ;
        if (theUserExists.isEnabled())
        {
            // TODO check of attributes are the same and
            // TODO if email not confirmed send confirmation email

            throw  new IllegalArgumentException("account already activated ");
        }
        String encodedPassword = passwordEncoder
                .encode(appUser.getPassword());

        theUserExists.setPassword(encodedPassword);
        userRepository.save(theUserExists);
        String token = UUID.randomUUID().toString();
        // TODO : SEND CONFIRMATION TOKEN
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token ,
                LocalDateTime.now() ,
                LocalDateTime.now().plusMinutes(15),
                theUserExists

        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        // TODO : SEND EMAIL
        return  token ;
    }

    @Transactional
    public String confirmToken(String token)
    {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
                .orElseThrow(
                        () -> new IllegalStateException("token not found"));
        if (confirmationToken.getConfirmedAt()!=null)
        {
            throw new IllegalStateException("token already confirmed");
        }
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now()))
        {
            throw new IllegalStateException("token expired");
        }
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        enable(confirmationToken.getAppUser().getEmail());
        return "confirmed";

    }

    public void enable(String email) {
        userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email))
        );
        if (userRepository.findByEmail(email).isPresent())
        {
            User appUser = userRepository.findByEmail(email).get();
            appUser.setEnabled(true);
            userRepository.save(appUser);
        }
    }
}
