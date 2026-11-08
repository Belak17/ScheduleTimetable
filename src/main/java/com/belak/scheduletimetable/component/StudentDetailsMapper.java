package com.belak.scheduletimetable.component;

import com.belak.scheduletimetable.dto.student.StudentDetailsDto;
import com.belak.scheduletimetable.model.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentDetailsMapper {
    public StudentDetailsDto convertToStudentDetailsDto(Student student) {
        return  new StudentDetailsDto(
                student.getId(),
                student.getUserId(),
                student.getNom(),
                student.getPrenom(),
                student.getPassword(),
                student.getEmail(),
                student.getTelephone(),
                student.getSexe(),
                student.getNationalite(),
                student.getDepartment(),
                student.getFiliere(),
                student.getNiveau(),
                student.getGroup(),
                student.isEnabled()
        );
    }
}
