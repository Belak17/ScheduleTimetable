package com.belak.scheduletimetable.dto;

import com.belak.scheduletimetable.enumeration.Grade;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfessorDto {
    private  String userId ;
    private String nom ;
    private  String prenom ;
    private Grade grade ;



}
