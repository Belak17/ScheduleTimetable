package com.belak.scheduletimetable.component;

import com.belak.scheduletimetable.dto.StudentDto;
import com.belak.scheduletimetable.model.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {
    public StudentDto convertToDto(Student student)
    {
        return new StudentDto(student.getUserId(),
                student.getDepartment().getLibelle()
                ,student.getFiliere().getCode(),
                student.getGroup() ,
                student.getNiveau(),
                student.getNom(),
                student.getPrenom(),
                student.getEmail()
        );
    }
}
