package com.belak.scheduletimetable.component;

import com.belak.scheduletimetable.dto.professor.ProfessorDto;
import com.belak.scheduletimetable.model.Professor;
import org.springframework.stereotype.Component;

@Component
public class ProfessorMapper {
    public ProfessorDto convertToDto(Professor professor)
    {
        return new ProfessorDto(
                professor.getIdUser(),
                professor.getUserId(),
                professor.getNom()
                ,professor.getPrenom(),
                professor.getGrade().getLibelle());
    }
}
