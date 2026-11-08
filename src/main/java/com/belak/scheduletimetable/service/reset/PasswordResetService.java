package com.belak.scheduletimetable.service.reset;

import com.belak.scheduletimetable.exception.ResetNotPossibleAccountNotActivatedException;
import com.belak.scheduletimetable.model.ResetToken;
import com.belak.scheduletimetable.model.User;
import com.belak.scheduletimetable.repository.user.ResetPasswordTokenRepository;
import com.belak.scheduletimetable.repository.user.UserRepository;
import com.belak.scheduletimetable.service.EmailUtilsService;
import com.belak.scheduletimetable.utils.EmailSender;
import com.belak.scheduletimetable.utils.EmailValidator;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailValidator emailValidator;

    private final EmailSender emailSender;

    private final EmailUtilsService emailUtilsService;

    private final ResetPasswordTokenRepository resetPasswordTokenRepository;


    private static final int TOKEN_EXPIRATION_MINUTES = 15;

    private static final int MIN_PASSWORD_LENGTH = 8;

    private static final String SUCCESS_MESSAGE =
            "Si cette adresse email est associée à un compte actif, " +
                    "vous recevrez un lien de réinitialisation.";

    private  static  final String USER_NOT_FOUND = "Cet email n'existe pas. Assurez-vous d'entrer correctement votre " +
            "email ";

    private  static  final String USER_NOT_ACTIVATED = "Ce compte n'est pas activé . Réinitialisation " +
            "impossible";

    @Transactional
    public String resetPassword(String email) {

        if (email == null || !emailValidator.test(email.trim())) {
            throw new IllegalArgumentException("Email not valid");
        }

        String normalizedEmail = email.trim();

        User user = userRepository.findByEmail(normalizedEmail)
                .orElse(null);


        if (user == null )
        {
            return USER_NOT_FOUND;
        }
        if (!user.isEnabled()) {
            return USER_NOT_ACTIVATED;
        }

        String token = reset(user);

        String baseUrl = "http://localhost:8082";
        String link = baseUrl
                + "/reset/password?token="
                + token;

        emailSender.sendEmail(
                user.getEmail(),
                "Réinitialisation de votre mot de passe",
                buildEmail(user.getPrenom(), link)
        );

        return SUCCESS_MESSAGE;
    }



    @Transactional
    public String reset(User appUser) {

        if (!appUser.isEnabled()) {

            throw new ResetNotPossibleAccountNotActivatedException(
                    "Votre compte est désactivé. " +
                            "Vous ne pouvez pas réinitialiser votre mot de passe."
            );
        }

        /*
         * Invalidation des anciens tokens.
         */
        resetPasswordTokenRepository.deleteByAppUser(appUser);

        /*
         * Génération d'un nouveau token.
         */
        String token = UUID.randomUUID().toString();

        LocalDateTime now = LocalDateTime.now();

        ResetToken resetToken = new ResetToken(
                token,
                now,
                now.plusMinutes(TOKEN_EXPIRATION_MINUTES),
                appUser
        );

        resetPasswordTokenRepository.save(resetToken);

        return token;
    }



    public String buildEmail(String name, String link) {

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
                        Réinitialisation de votre mot de passe
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
                        Nous avons reçu une demande de réinitialisation
                        du mot de passe associé à votre compte.
                    </p>

                    <p style="
                        color: #475569;
                        font-size: 15px;
                        line-height: 1.6;
                    ">
                        Pour définir un nouveau mot de passe, veuillez
                        cliquer sur le bouton ci-dessous :
                    </p>

                    <!-- Reset Button -->
                    <div style="
                        margin: 30px auto;
                    ">
                        <a href="%s" style="
                            display: inline-block;
                            padding: 14px 28px;
                            background-color: #2563eb;
                            color: #ffffff;
                            text-decoration: none;
                            font-size: 15px;
                            font-weight: bold;
                            border-radius: 8px;
                        ">
                            Réinitialiser mon mot de passe
                        </a>
                    </div>

                    <p style="
                        color: #64748b;
                        font-size: 14px;
                        line-height: 1.5;
                    ">
                        Ce lien est valable pendant
                        <strong style="color: #2563eb;">
                            15 minutes
                        </strong>.
                    </p>

                    <p style="
                        color: #94a3b8;
                        font-size: 13px;
                        line-height: 1.5;
                        margin-top: 25px;
                    ">
                        Si vous n'êtes pas à l'origine de cette demande,
                        vous pouvez ignorer cet email.
                        Votre mot de passe actuel restera inchangé.
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
        """.formatted(name, link);
    }



    @Transactional
    public String confirmResetToken(String token, String password) {

        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Invalid token");
        }

        /*
         * Recherche du token.
         */
        ResetToken resetToken = resetPasswordTokenRepository
                .findByToken(token)
                .orElseThrow(
                        () -> new IllegalStateException("Token not found")
                );

        /*
         * Vérification de l'utilisation du token.
         */
        if (resetToken.getConfirmedAt() != null) {

            return "Ce lien a déjà été utilisé pour réinitialiser votre mot de passe";
        }

        /*
         * Vérification de l'expiration.
         */
        if (!resetToken.getExpiresAt().isAfter(LocalDateTime.now())) {

            return "Ce lien est expiré . Veuillez en demander un nouveau";
        }

        User user = resetToken.getAppUser();

        /*
         * Vérification de l'état du compte.
         */
        if (!user.isEnabled()) {

            return USER_NOT_ACTIVATED;
        }

        /*
         * Validation du nouveau mot de passe.
         */
        validatePassword(password);

        /*
         * Confirmation du token.
         */
        resetToken.setConfirmedAt(LocalDateTime.now());


        // Envoi email avec information du compte
        emailSender.sendEmail(user.getEmail(),"Informations du compte",
                emailUtilsService.buildCredentialsEmail(user.getPrenom()+" "+user.getNom(), user.getUserId(),password));

        /*
         * Mise à jour du mot de passe.
         */
        resetUserPassword(user, password);



        return "Votre mot de passe a bien été changé";
    }



    private void validatePassword(String password) {

        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {

            throw new IllegalArgumentException(
                    "Le mot de passe doit contenir au moins "
                            + MIN_PASSWORD_LENGTH + " caractères."
            );
        }
    }



    private void resetUserPassword(User user, String password) {

        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);
    }

}