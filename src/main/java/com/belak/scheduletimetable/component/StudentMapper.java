package com.belak.scheduletimetable.component;

import com.belak.scheduletimetable.dto.student.StudentDto;
import com.belak.scheduletimetable.model.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {
    public StudentDto convertToDto(Student student)
    {
        return new StudentDto(
                student.getIdUser(),
                student.getUserId(),
                student.getFiliere() != null ? student.getFiliere().getCode() : "",
                student.getGroup(),
                student.getNiveau(),
                student.getNom(),
                student.getPrenom(),
                student.getEmail()
        );
    }
}
