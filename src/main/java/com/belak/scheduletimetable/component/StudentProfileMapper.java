package com.belak.scheduletimetable.component;

import com.belak.scheduletimetable.dto.StudentProfileDto;
import com.belak.scheduletimetable.enumeration.Filiere;
import com.belak.scheduletimetable.model.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentProfileMapper {
    public StudentProfileDto convertToStudentProfileDto(Student student)
    {
        return new StudentProfileDto(student.getCin(), student.getNom(), student.getPrenom(),
                student.getEmail(),student.getTelephone(),
                student.getSexe(),student.getVilleNaissance(), student.getAdresse() , student.getCodePostal(),
                student.getVille(),  student.getNationalite() , student.getDepartment().getLibelle() , student.getFiliere().getLibelle() ,
                student.getNiveau() , student.getGroup()
        ) ;
    }
}
