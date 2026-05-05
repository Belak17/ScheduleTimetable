package com.belak.scheduletimetable.component;

import com.belak.scheduletimetable.dto.ProfessorDto;
import com.belak.scheduletimetable.model.Professor;
import org.springframework.stereotype.Component;

@Component
public class ProfessorMapper {
    public ProfessorDto convertToDto(Professor professor)
    {
        return new ProfessorDto(professor.getUserId(),
                professor.getNom()
                ,professor.getPrenom(),
                professor.getGrade().toString()
                ,professor.getSchoolStatus().toString(),
                professor.getSpecialite());
    }
}
