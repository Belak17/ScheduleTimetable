package com.belak.scheduletimetable.dto;

import com.belak.scheduletimetable.enumeration.Departement;
import com.belak.scheduletimetable.enumeration.Grade;
import com.belak.scheduletimetable.enumeration.Nationalite;
import com.belak.scheduletimetable.enumeration.Statuts;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProfessorImportDto {
    private  String userId ;
    private  String cin ;
    private  String nom ;
    private  String prenom ;
    private String email ;
    private  String  telephone;
    private  String sexe ;
    private LocalDate dateNaissance;
    private String villeNaissance;
    private String adresse ;
    private String codePostal;
    private String ville ;
    private Nationalite nationalite ;
    private Departement departement ;
    private String specialite ;
    private Statuts schoolStatus ;
    private Grade grade ;
    private  String rib ;
    private String etablissement_origine ;
    private  String application_tiers ;

}
