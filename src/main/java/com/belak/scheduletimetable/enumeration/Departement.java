package com.belak.scheduletimetable.enumeration;



public enum Departement {

    PHY("PHY", "Physique"),
    CHI("CHI", "Chimie"),
    MAT("MAT", "Mathematique"),
    INF("INF", "Informatique"),
    SV("SV", "Biologie"),
    ST("ST", "Geologie");
//    UT("UT", "Unité Transversale");

    private final String code;
    private final String libelle;

    Departement(String code, String libelle) {
        this.code = code;
        this.libelle = libelle;
    }

    public String getCode() {
        return code;
    }

    public String getLibelle() {
        return libelle;
    }

    // Méthode utilitaire pour récupérer une constante à partir du libellé
    public static Departement fromLibelle(String libelle) {
        if (libelle == null) {
            throw new IllegalArgumentException("Libellé null");
        }

        for (Departement d : Departement.values()) {
            if (d.getLibelle().equalsIgnoreCase(libelle.trim())) {
                return d;
            }
        }

        throw new IllegalArgumentException("Département inconnu: " + libelle);
    }
}
