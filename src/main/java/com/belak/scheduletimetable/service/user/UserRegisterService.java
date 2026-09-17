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

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

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
    public int register(UserRegister request)
    {
        boolean isValidEmail = emailValidator
                .test(request.getEmail());
        if (!isValidEmail)
        {
            throw new IllegalArgumentException("email not valid");
        }
        int  token = signup(
                new User(
                        request.getUserId(),
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User theUser = userRepository.findByEmail(request.getEmail()).get();
        //String link = "http://localhost:8082/confirm?token=" + token;
        emailSender.sendEmail(
                request.getEmail(),"Registration Confirmation",
                buildEmail(theUser.getPrenom(),token));
        return token ;
    }

    public  String buildEmail(String name, int token) {

        return """
        <!DOCTYPE html>
        <html lang="fr">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
        </head>

        <body style="
            margin: 0;
            padding: 0;
            background-color: #f1f5f9;
            font-family: Arial, Helvetica, sans-serif;
        ">

            <div style="
                max-width: 600px;
                margin: 40px auto;
                background-color: #ffffff;
                border-radius: 12px;
                overflow: hidden;
                box-shadow: 0 4px 15px rgba(0,0,0,0.08);
            ">

                <!-- Header -->
                <div style="
                    background-color: #2e2e4c;
                    padding: 25px;
                    text-align: center;
                ">
                    <h1 style="
                        margin: 0;
                        color: #ffffff;
                        font-size: 22px;
                    ">
                        Faculté des Sciences de Gabès
                    </h1>
                </div>


                <!-- Content -->
                <div style="
                    padding: 35px 30px;
                    text-align: center;
                ">

                    <h2 style="
                        color: #073772;
                        margin-top: 0;
                        margin-bottom: 15px;
                    ">
                        Vérification de votre compte
                    </h2>

                    <p style="
                        color: #475569;
                        font-size: 15px;
                        line-height: 1.6;
                    ">
                        Bonjour <strong>%s</strong>,
                    </p>

                    <p style="
                        color: #475569;
                        font-size: 15px;
                        line-height: 1.6;
                    ">
                        Merci de vous être inscrit sur la plateforme
                        de la Faculté des Sciences de Gabès.
                    </p>

                    <p style="
                        color: #475569;
                        font-size: 15px;
                        line-height: 1.6;
                    ">
                        Pour activer votre compte, veuillez utiliser
                        le code de vérification suivant :
                    </p>


                    <!-- OTP -->
                    <div style="
                        margin: 30px auto;
                        padding: 18px;
                        width: 200px;
                        background-color: #eff6ff;
                        border: 1px solid #bfdbfe;
                        border-radius: 10px;
                    ">
                        <span style="
                            font-size: 32px;
                            font-weight: bold;
                            letter-spacing: 8px;
                            color: #2563eb;
                        ">
                            %s
                        </span>
                    </div>


                    <p style="
                        color: #64748b;
                        font-size: 14px;
                        line-height: 1.5;
                    ">
                        Ce code est valable pendant
                        <strong style="color: #2563eb;">
                            5 minutes
                        </strong>.
                    </p>

                    <p style="
                        color: #94a3b8;
                        font-size: 13px;
                        margin-top: 25px;
                    ">
                        Si vous n'êtes pas à l'origine de cette demande,
                        vous pouvez ignorer cet email.
                    </p>

                </div>


                <!-- Footer -->
                <div style="
                    background-color: #f8fafc;
                    border-top: 1px solid #e2e8f0;
                    padding: 18px;
                    text-align: center;
                ">

                    <p style="
                        margin: 0;
                        color: #64748b;
                        font-size: 12px;
                    ">
                        © Faculté des Sciences de Gabès -
                        Tous droits réservés
                    </p>

                </div>

            </div>

        </body>
        </html>
        """.formatted(name, token);
    }

    public int signup(User appUser)
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
        SecureRandom random = new SecureRandom();

        int token = 100000 + random.nextInt(900000);
        // TODO : SEND CONFIRMATION TOKEN
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token ,
                LocalDateTime.now() ,
                LocalDateTime.now().plusMinutes(5),
                theUserExists

        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        // TODO : SEND EMAIL
        return  token ;
    }

    @Transactional
    public String confirmToken(int token)
    {
        Optional<ConfirmationToken> optionalToken =
                confirmationTokenService.getToken(token);

        if (optionalToken.isEmpty()) {
            return "token not found";
        }

        ConfirmationToken confirmationToken = optionalToken.get();
        if (confirmationToken.getConfirmedAt()!=null)
        {
            return "token already confirmed";
        }
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now()))
        {
            return "token expired";
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
