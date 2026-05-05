package com.belak.scheduletimetable.component;

import com.belak.scheduletimetable.dto.ProfessorDto;
import com.belak.scheduletimetable.dto.ProfessorTimetableDto;
import com.belak.scheduletimetable.model.ProfessorTimetable;
import com.belak.scheduletimetable.service.professor.ProfessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfessorTimetableMapper {
    private final ProfessorService professorService ;
    private  final ProfessorMapper professorMapper ;
    public ProfessorTimetableDto convertToDto(ProfessorTimetable timetable)
    {
        ProfessorDto professorDto = professorMapper.convertToDto(timetable.getProfessor());
        return new ProfessorTimetableDto(timetable.getId(),
                timetable.getSpeciality(),
                timetable.getStatut().toString() , professorDto);
    }
}
