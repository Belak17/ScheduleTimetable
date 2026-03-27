package com.belak.scheduletimetable.dto;

import com.belak.scheduletimetable.enumeration.Departement;
import com.belak.scheduletimetable.enumeration.Nationalite;
import com.belak.scheduletimetable.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@SuperBuilder
public class CreateUserDto {

    private String userId;
    private String cin ;
    private String nom; //nom
    private String prenom;//prenom
    private String prenomArabe;
    private String nomArabe;
    private String email ;
    private String telephone;
    private String sexe;
    private LocalDate dateNaissance;
    private String villeNaissance;
    private String villeNaissanceArabe;
    private String adresse;
    private String codePostal;
    private String ville;
    private Nationalite nationalite;
    private Departement department ;
    private String codeDepartement;
    private User.Role role = User.Role.USER;
    private String password ;
}
