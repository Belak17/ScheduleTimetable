package com.belak.scheduletimetable.dto.professor;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfessorDto {
    private Long id ;
    private  String userId ;
    private String nom ;
    private  String prenom ;
    private String grade ;



}
