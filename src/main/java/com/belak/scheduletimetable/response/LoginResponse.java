package com.belak.scheduletimetable.response;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class LoginResponse {

    private String nom;
    private String prenom;

    public void setNom(String nom) {
        this.nom = nom != null ? nom.trim() : null;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom != null ? prenom.trim() : null;
    }
}