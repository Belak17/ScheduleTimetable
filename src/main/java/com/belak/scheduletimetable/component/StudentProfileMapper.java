package com.belak.scheduletimetable.component;

import com.belak.scheduletimetable.dto.StudentProfileDto;
import com.belak.scheduletimetable.enumeration.Filiere;
import com.belak.scheduletimetable.enumeration.Nationalite;
import com.belak.scheduletimetable.model.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentProfileMapper {
    public StudentProfileDto convertToStudentProfileDto(Student student)
    {
        return new StudentProfileDto(
                student.getUserId() != null ? student.getUserId() : "",
                student.getNom() != null ? student.getNom() : "",
                student.getPrenom() != null ? student.getPrenom() : "",
                student.getPassword() != null ? student.getPassword() : "",
                student.getEmail() != null ? student.getEmail() : "",
                student.getTelephone() != null ? student.getTelephone() : "",
                student.getSexe() != null ? student.getSexe() : "",
                student.getNationalite() != null
                        ? student.getNationalite().getLibelleNationalite() : "",
                student.getTimetables() != null && !student.getTimetables().isEmpty()
                        && student.getTimetables().iterator().next().getDepartement() != null
                        ? student.getTimetables().iterator().next().getDepartement().getLibelle() : "",
                student.getFiliere() != null ? student.getFiliere().getLibelle() : "",
                student.getNiveau() != null ? student.getNiveau() : "",
                student.getGroup() != null ? student.getGroup() : ""
        );
    }
}
