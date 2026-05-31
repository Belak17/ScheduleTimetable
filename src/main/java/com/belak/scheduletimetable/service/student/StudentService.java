package com.belak.scheduletimetable.service.student;

import com.belak.scheduletimetable.component.PresenceMapper;
import com.belak.scheduletimetable.component.StudentMapper;
import com.belak.scheduletimetable.component.StudentProfileMapper;
import com.belak.scheduletimetable.dto.CreateStudentDto;
import com.belak.scheduletimetable.dto.PresenceDto;
import com.belak.scheduletimetable.dto.StudentDto;
import com.belak.scheduletimetable.dto.StudentProfileDto;
import com.belak.scheduletimetable.enumeration.Filiere;

import com.belak.scheduletimetable.exception.ResourceNotFoundException;

import com.belak.scheduletimetable.model.CoursTP;
import com.belak.scheduletimetable.model.Seance;
import com.belak.scheduletimetable.model.Student;
import com.belak.scheduletimetable.repository.PresenceRepository;
import com.belak.scheduletimetable.repository.StudentRepository;
import lombok.RequiredArgsConstructor;

import org.apache.commons.math3.linear.DefaultIterativeLinearSolverEvent;
import org.apache.poi.ss.usermodel.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.List;


@Service
@RequiredArgsConstructor
public class StudentService {
    private  final StudentRepository studentRepository ;
    private  final StudentMapper studentMapper ;
    private  final PresenceRepository presenceRepository ;
    private  final StudentProfileMapper profileMapper ;
    private  final PasswordEncoder passwordEncoder ;
    public Page<StudentDto> getStudentByFieldAndYearAndGroup(String field,int year,String group,int page,int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return studentRepository.findByFiliereAndNiveauAndGroup(Filiere.valueOf(field), year,group,pageable).map(studentMapper::convertToDto);
    }
    public void saveStudent(CreateStudentDto studentDto)
    {
        Student student = new Student(studentDto);
        studentRepository.save(student);
    }
    public void deleteStudent(String userId)
   {
       studentRepository.deleteByUserId(userId) ;
   }
    public StudentProfileDto findByUserId(String userId)
    {
        return profileMapper.convertToStudentProfileDto(studentRepository
                .findByUserId(userId).get());
    }

    public void updateEmail(String nouvEmail, String confEmail, String userId) {
        if (!nouvEmail.equals(confEmail)) {
            throw new IllegalArgumentException("Les emails ne correspondent pas");
        }

        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Étudiant introuvable"));

        student.setEmail(nouvEmail);
        studentRepository.save(student);
    }

    public void updateInfo(String userId, String address, String telephone, String code) {
        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Étudiant introuvable"));
        student.setUserId(userId);
        student.setAdresse(address);
        student.setTelephone(telephone);
        student.setCodePostal(code);
        studentRepository.save(student);
    }

    public void updatePassword(String oldPassword,
                               String newPassword,
                               String confPassword,
                               String userId) {

        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Étudiant introuvable"));

        if (!passwordEncoder.matches(oldPassword, student.getPassword())) {
            throw new IllegalArgumentException("Ancien mot de passe incorrect");
        }

        if (!newPassword.equals(confPassword)) {
            throw new IllegalArgumentException("Les mots de passe ne correspondent pas");
        }

        student.setPassword(passwordEncoder.encode(newPassword));
        studentRepository.save(student);
    }
}
