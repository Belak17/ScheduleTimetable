package com.belak.scheduletimetable.dto;

import com.belak.scheduletimetable.enumeration.*;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StudentImportDto {
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
    private Filiere filiere ;
    private Integer niveau;

    private TypeDiplome typeDiplome;

    private String numeroInscription;

    private String groupeC;
    private String groupeTD;
    private String groupeM;
    private String groupeArchive;

    private String group ;

}
