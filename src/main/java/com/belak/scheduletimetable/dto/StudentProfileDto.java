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
    private  String userId ;


    private String nom; //nom
    private String prenom;//prenom


    private  String password ;
    private String email ;
    private String telephone;
    private String sexe;

    private Nationalite nationalite;

    private String department ;

    private String field;
    private String niveau;
    private String group ;
}
