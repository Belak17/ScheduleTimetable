package com.belak.scheduletimetable.enumeration;

import java.util.Arrays;

public enum Groupe {

    TD1TP1("A", "TD1 TP1"),
    TD1TP2("B", "TD1 TP2"),
    TD2TP1("C", "TD2 TP1"),
    TD2TP2("D", "TD2 TP2"),
    TD3TP1("E", "TD3 TP1"),
    TD3TP2("F", "TD3 TP2"),
    TD4TP1("G", "TD4 TP1"),
    TD4TP2("H", "TD4 TP2"),
    TD5TP1("I", "TD5 TP1"),
    TD5TP2("J", "TD5 TP2"),
    TD6TP1("K", "TD6 TP1"),
    TD6TP2("L", "TD6 TP2"),
    TD7TP1("M", "TD7 TP1"),
    TD7TP2("N", "TD7 TP2"),
    TD8TP1("O", "TD8 TP1"),
    TD8TP2("P", "TD8 TP2"),
    TD9TP1("Q", "TD9 TP1"),
    TD9TP2("R", "TD9 TP2"),
    TD10TP1("S", "TD10 TP1"),
    TD10TP2("T", "TD10 TP2");

    private final String code;
    private final String libelle;

    Groupe(String code, String libelle) {
        this.code = code;
        this.libelle = libelle;
    }


    public String getCode() {
        return code;
    }

    public String getLibelle() {
        return libelle;
    }

    public static Groupe fromCode(String code) {
        if (code == null || code.isBlank()) {
            return null;
        }

        return Arrays.stream(values())
                .filter(groupe -> groupe.code.equalsIgnoreCase(code.trim()))
                .findFirst()
                .orElse(null);
    }


    public static Groupe fromLibelle(String libelle) {
        if (libelle == null || libelle.isBlank()) {
            return null;
        }

        return Arrays.stream(values())
                .filter(groupe ->
                        groupe.libelle.equalsIgnoreCase(libelle.trim()))
                .findFirst()
                .orElse(null);
    }


    public static Groupe fromName(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }

        try {
            return Groupe.valueOf(name.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }


    public static boolean existsCode(String code) {
        return fromCode(code) != null;
    }


    public static boolean existsLibelle(String libelle) {
        return fromLibelle(libelle) != null;
    }
}
