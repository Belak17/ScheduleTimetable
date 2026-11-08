package com.belak.scheduletimetable.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailUtilsService {
    public String buildCredentialsEmail(String name, String identifier, String password) {

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
                    Bienvenue sur votre espace 
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
                    Votre compte a été créé avec succès.
                    Vous trouverez ci-dessous vos identifiants
                    de connexion pour accéder à votre espace personnel.
                </p>

                <!-- Credentials -->
                <div style="
                    margin: 30px auto;
                    padding: 22px;
                    background-color: #f8fafc;
                    border: 1px solid #e2e8f0;
                    border-radius: 10px;
                    text-align: left;
                ">

                    <p style="
                        margin: 0 0 18px 0;
                        color: #64748b;
                        font-size: 13px;
                    ">
                        IDENTIFIANT
                    </p>

                    <p style="
                        margin: -10px 0 22px 0;
                        color: #073772;
                        font-size: 17px;
                        font-weight: bold;
                    ">
                        %s
                    </p>

                    <p style="
                        margin: 0 0 18px 0;
                        color: #64748b;
                        font-size: 13px;
                    ">
                        MOT DE PASSE
                    </p>

                    <p style="
                        margin: -10px 0 0 0;
                        color: #073772;
                        font-size: 17px;
                        font-weight: bold;
                    ">
                        %s
                    </p>

                </div>

                <p style="
                    color: #475569;
                    font-size: 14px;
                    line-height: 1.6;
                ">
                    Nous vous recommandons de modifier votre mot de passe
                    dès votre première connexion afin de garantir
                    la sécurité de votre compte.
                </p>

                <p style="
                    color: #94a3b8;
                    font-size: 13px;
                    line-height: 1.5;
                    margin-top: 25px;
                ">
                    Si vous n'êtes pas à l'origine de la création
                    de ce compte, veuillez contacter l'administration
                    de la Faculté des Sciences de Gabès.
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
    """.formatted(name, identifier, password);
    }
}
