package com.belak.scheduletimetable.dto.professor;

import com.belak.scheduletimetable.enumeration.Departement;
import com.belak.scheduletimetable.enumeration.Grade;
import com.belak.scheduletimetable.enumeration.Nationalite;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ProfessorDetailsDto {

    private long id;

    private  String userId ;

    private  String nom ;
    private  String prenom ;

    private String email ;
    private  String  telephone;
    private  String sexe ;
    private LocalDate dateNaissance;

    private Grade grade ;

    private Nationalite nationalite;

    private Departement departement;

    private boolean enabled;
}
