package com.belak.scheduletimetable.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfessorDto {
    private  String userId ;
    private String nom ;
    private  String prenom ;
    private  String grade ;
    private String statut ;
    private String speciality ;


}
