package com.belak.scheduletimetable.component;

import com.belak.scheduletimetable.dto.professor.ProfessorProfileDto;
import com.belak.scheduletimetable.enumeration.Nationalite;
import com.belak.scheduletimetable.model.Professor;
import org.springframework.stereotype.Component;

@Component
public class ProfessorProfileMapper {
    public ProfessorProfileDto convertToProfessorProfileDto(Professor professor)
    {
        return new ProfessorProfileDto(
                professor.getUserId() != null ? professor.getUserId() : "",
                professor.getNom() != null ? professor.getNom() : "",
                professor.getPrenom() != null ? professor.getPrenom() : "",
                professor.getEmail() != null ? professor.getEmail() : "",
                professor.getTelephone() != null ? professor.getTelephone() : "",
                professor.getSexe() != null ? professor.getSexe() : "",
                professor.getNationalite() != null ? professor.getNationalite() : Nationalite.AU,
                professor.getDepartment() != null && professor.getDepartment().getLibelle() != null
                        ? professor.getDepartment().getLibelle() : "",
                professor.getGrade() != null && professor.getGrade().getLibelle() != null
                        ? professor.getGrade().getLibelle() : ""
        );
    }
}
