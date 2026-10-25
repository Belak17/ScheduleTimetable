package com.belak.scheduletimetable.enumeration;

import java.util.Arrays;
import java.util.Locale;

public enum Filiere {

    LCHI("LCH", "LICENCE EN CHIMIE"),
    LCHR("LCHR", "LICENCE EN CHIMIE R"),
    LPHY("LPH", "LICENCE EN PHYSIQUE"),
    LMAT("LMA", "LICENCE EN MATHEMATIQUES"),
    LMa("LMa", "LICENCE EN MATHEMATIQUES"),
    LM("LM", "LICENCE EN MATHEMATIQUES"),
    LSV("LSV", "LICENCE EN SCIENCE DE LA VIE"),
    LST("LST", "LICENCE EN SCIENCE DE LA TERRE"),
    LGEOR("LGEOR", "LICENCE EN SCIENCE DE LA TERRE"),
    LGD("LGD", "LICENCE EN SCIENCE DE LA TERRE"),
    LSVT("LSVT", "LICENCE EN SCIENCE DE LA VIE ET DE LA TERRE"),
    LSI("LGLSI", "LICENCE EN SCIENCES INFORMATIQUES"),
    LISI("LIRIS", "LICENCE EN INGENIERIE DES SYSTEMES INFORMATIQUES"),
    LIRS("LIRS", "LICENCE EN INGENIERIE DES SYSTEMES INFORMATIQUES"),
    LMPI("LMPI", "LICENCE EN MESURES PHYSIQUES ET INSTRUMENTATIONS"),
    LBIO("LBT", "LICENCE EN BIOTECHNOLOGIE"),
    LBMC("LBMC", "LICENCE EN BIOTECHNOLOGIE"),
    LEEA("LEEA", "LICENCE EN ELECTRONIQUE ELECTROTECHNIQUE ET AUTOMATISME"),

    MR_MATH("MR-MATH", "MASTER DE RECHERCHE EN MATHEMATIQUES"),
    MR_PVMA("MR-PVMA", "MASTER DE RECHERCHE EN PATHOGENIE VIRULENCE DES MICROORGANISMES ET AGENTS"),
    MR_CME("MR-CME", "MASTER DE RECHERCHE EN CHIMIE MATERIAUX ET ENVIRONNEMENT"),
    MR_PMN("MR-PMN", "MASTER DE RECHERCHE EN PHYSIQUE DES MATERIAUX ET DES NANOMATERIAUX"),
    MR_PH("MR-PH", "MASTER DE RECHERCHE EN PHYSIQUE DES MATERIAUX ET DES NANOMATERIAUX"),
    MP_SSRI("MP-SSRI", "MASTER PROFESSIONNEL EN SECURITES DES SYSTEMES ET DES RESEAUX INFORMATIQUES"),
    MP_VGM("MP-VGM", "MASTER PROFESSIONNEL EN VALORISATION DES GEO-MATERIAUX"),
    MR_MGB("MR-MGB", "MASTER DE RECHERCHE EN MICROBIOLOGIE, GENETIQUE ET BIORESSOURCES"),
    MR_GBS("MR-GBS", "MASTER DE RECHERCHE EN GEOLOGIE DES BASSINS SEDIMENTAIRES"),
    MR_BOPE("MR-BOPE", "MASTER DE RECHERCHE EN BIOLOGIE DES ORGANISMES, DES POPULATIONS ET ENVIRONNEMENT"),
    //MR_BOPEA("MR-BOPEA", "MASTER DE RECHERCHE EN BIOLOGIE DES ORGANISMES, DES POPULATIONS ET ENVIRONNEMENT"),
    MR_IR("MR-IR", "MASTER DE RECHERCHE EN INFORMATIQUE ET RESEAUX"),

    LBDA("LBDA", "LICENCE BIG DATA AND DATA ANALYSIS"),

    LBIO_ALIAS("LBIO", "LICENCE EN BIOTECHNOLOGIE"),

    LISI_ALIAS("LISI", "LICENCE EN INGENIERIE DES SYSTEMES INFORMATIQUES"),

    LSI_ALIAS("LSI", "LICENCE EN SCIENCES INFORMATIQUES"),

    LMI("LMI", "LICENCE EN MATHEMATIQUES"),

    LMP("LMP", "LICENCE MATHEMATIQUES APPLIQUEES"),

    LIMDM("LIMDM", "LICENCE EN INSTRUMENTATION ET MAINTENANCE DES DISPOSITIFS MEDICAUX"),

    LIMDMA("LIMDMA", "LICENCE EN INSTRUMENTATION ET MAINTENANCE DES DISPOSITIFS MEDICAUX"),

    LIMDMB("LIMDMB", "LICENCE EN INSTRUMENTATION ET MAINTENANCE DES DISPOSITIFS MEDICAUX");

    private final String code;
    private final String libelle;

    Filiere(String code, String libelle) {
        this.code = code;
        this.libelle = libelle;
    }

    public String getCode() {
        return code;
    }

    public String getLibelle() {
        return libelle;
    }

    public static Filiere fromCode(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException(
                    "Le code de la filière ne peut pas être vide."
            );
        }

        // Normaliser le code reçu
        String normalizedCode = code.trim()
                .replace("-", "")
                .toUpperCase(Locale.ROOT);

        // Gérer les codes alternatifs
        switch (normalizedCode) {
            case "LBDDA":
                normalizedCode = "LBDA";
                break;

            case "MPSSIR":
                normalizedCode = "MPSSRI";
                break;
            case "MRMA":
                normalizedCode = "MRMATH";
                break;
            case "MRPVM":
                normalizedCode = "MRPVMA";
                break;
            case "LIRS" :
                normalizedCode = "LIRIS";
                break;
            case "MRBOPEA" :
                normalizedCode = "MRBOPE";
                break;




            default:
                break;
        }

        final String finalCode = normalizedCode;

        return Arrays.stream(values())
                .filter(filiere ->
                        filiere.getCode()
                                .replace("-", "")
                                .equalsIgnoreCase(finalCode)
                )
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Code inconnu pour Filiere : " + code
                        )
                );
    }
    public static String getLabelByEnumName(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }

        try {
            return Filiere.valueOf(name.trim()).getLibelle();
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static Filiere fromLibelle(String libelle) {
        if (libelle == null || libelle.isBlank()) {
            return null;
        }

        return Arrays.stream(values())
                .filter(filiere ->
                        filiere.getLibelle().equalsIgnoreCase(libelle.trim())
                )
                .findFirst()
                .orElse(null);
    }
    public static String normalizeGroup(String group) {
        if (group == null || group.isBlank()) {
            return group;
        }

        String normalizedGroup = group.trim();

        for (Filiere filiere : values()) {
            String code = filiere.getCode();

            // Vérifier si le groupe commence par le code de la filière
            if (normalizedGroup.regionMatches(true, 0, code, 0, code.length())) {

                // Vérifier que le code est suivi d'un chiffre
                if (normalizedGroup.length() > code.length()
                        && Character.isDigit(normalizedGroup.charAt(code.length()))) {

                    Filiere canonicalFiliere = fromCode(code);

                    return canonicalFiliere.getCode()
                            + normalizedGroup.substring(code.length());
                }
            }
        }

        return normalizedGroup;
    }
}