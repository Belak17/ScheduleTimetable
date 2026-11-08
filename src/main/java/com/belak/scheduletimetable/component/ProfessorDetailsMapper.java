package com.belak.scheduletimetable.component;

import com.belak.scheduletimetable.dto.professor.ProfessorDetailsDto;
import com.belak.scheduletimetable.dto.professor.ProfessorProfileDto;
import com.belak.scheduletimetable.enumeration.Nationalite;
import com.belak.scheduletimetable.model.Professor;
import org.springframework.stereotype.Component;

@Component
public class ProfessorDetailsMapper {
    public ProfessorDetailsDto convertToProfessorDetailsDto(Professor professor)
    {
        return new ProfessorDetailsDto(
                professor.getIdUser(),
                professor.getUserId() != null ? professor.getUserId() : "NA",
                professor.getNom() != null ? professor.getNom() : "NA",
                professor.getPrenom() != null ? professor.getPrenom() : "NA",
                professor.getEmail() != null ? professor.getEmail() : "NA",
                professor.getTelephone() != null ? professor.getTelephone() : "NA",
                professor.getSexe() != null ? professor.getSexe() : "NA",
                professor.getDateNaissance(),
                professor.getGrade() != null ? professor.getGrade() : null,
                professor.getNationalite() != null ? professor.getNationalite() : null,
                professor.getDepartment() != null ? professor.getDepartment() : null,
                professor.isEnabled()
        );
    }
}
