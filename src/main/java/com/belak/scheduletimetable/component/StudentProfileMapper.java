package com.belak.scheduletimetable.component;

import com.belak.scheduletimetable.dto.StudentProfileDto;
import com.belak.scheduletimetable.enumeration.Filiere;
import com.belak.scheduletimetable.model.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentProfileMapper {
    public StudentProfileDto convertToStudentProfileDto(Student student)
    {
        return new StudentProfileDto(student.getUserId() , student.getNom(), student.getPrenom(), student.getPassword(),
                student.getEmail(),student.getTelephone(),
                student.getSexe(),
                student.getNationalite() , student.getDepartment().getLibelle() , student.getFiliere().getLibelle() ,
                student.getNiveau() , student.getGroup()
        ) ;
    }
}
