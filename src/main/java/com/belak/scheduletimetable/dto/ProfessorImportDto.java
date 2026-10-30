package com.belak.scheduletimetable.dto;

import com.belak.scheduletimetable.enumeration.Departement;
import com.belak.scheduletimetable.enumeration.Grade;
import com.belak.scheduletimetable.enumeration.Nationalite;
import com.belak.scheduletimetable.enumeration.Statuts;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Data
public class ProfessorImportDto {
    private  String userId ;

    private  String nom ;
    private  String prenom ;

    private String email ;
    private  String  telephone;
    private  String sexe ;
    private LocalDate dateNaissance;

    private String grade ;

    private Nationalite nationalite;

    private Departement departement;

}
