package com.belak.scheduletimetable.component;

import com.belak.scheduletimetable.dto.ProfessorProfileDto;
import com.belak.scheduletimetable.dto.StudentProfileDto;
import com.belak.scheduletimetable.model.Professor;
import com.belak.scheduletimetable.model.Student;
import org.springframework.stereotype.Component;

@Component
public class ProfessorProfileMapper {
    public ProfessorProfileDto convertToProfessorProfileDto(Professor professor)
    {
        return new ProfessorProfileDto(professor.getCin(), professor.getNom(), professor.getPrenom(),
                professor.getEmail(), professor.getTelephone(),
                professor.getSexe(), professor.getVilleNaissance(), professor.getAdresse() , professor.getCodePostal(),
                professor.getVille(),  professor.getNationalite() , professor.getDepartment().getLibelle() , professor.getSpecialite()
                 , professor.getGrade().getLibelle()
        ) ;
    }
}
