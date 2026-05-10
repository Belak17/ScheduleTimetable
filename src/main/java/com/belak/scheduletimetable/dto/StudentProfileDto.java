package com.belak.scheduletimetable.dto;

import com.belak.scheduletimetable.enumeration.Departement;
import com.belak.scheduletimetable.enumeration.Filiere;
import com.belak.scheduletimetable.enumeration.Nationalite;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
@Data
@AllArgsConstructor
public class StudentProfileDto {
    private String cin ;
    private String nom; //nom
    private String prenom;//prenom

    private String email ;
    private String telephone;
    private String sexe;


    private String villeNaissance;


    private String adresse;
    private String codePostale;
    private String ville;
    private Nationalite nationalite;

    private String department ;

    private String field;
    private Integer niveau;
    private String group ;
}
