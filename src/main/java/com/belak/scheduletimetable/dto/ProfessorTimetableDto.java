package com.belak.scheduletimetable.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfessorTimetableDto {
    private  Long id ;
    private  String speciality ;
    private String grade ;
    private  String statut ;
    private ProfessorDto professorDto ;

    public ProfessorTimetableDto(Long id, String speciality, String statut, ProfessorDto professorDto) {
    }
}
