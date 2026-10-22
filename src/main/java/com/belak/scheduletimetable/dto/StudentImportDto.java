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
    private Long id;

    private String password;

    private String nom;

    private String prenom;

    private String userId;

    private Nationalite nationalite;

    private String sexe;

    @Column(unique = true, nullable = false)
    private String email;

    private LocalDate dateNaissance;

    private String isEmailVerified ;

    private String telephone;

    private Filiere filiere;

    private String niveau;

    private String codeDiplome;

    private String nomDiplome;

    private Departement departement ;
    private TypeDiplome typeDiplome;

    private String numeroInscription;

    private String group;

}
