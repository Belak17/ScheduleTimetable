package com.belak.scheduletimetable.dto;

import com.belak.scheduletimetable.enumeration.Nationalite;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfessorProfileDto {
    private  String userId ;

    private String nom; //nom
    private String prenom;//prenom


    private String email ;
    private String telephone;
    private String sexe;


    private Nationalite nationalite;

    private String department ;

    private String grade ;
}
