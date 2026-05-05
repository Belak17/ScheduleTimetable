package com.belak.scheduletimetable.service.student;

import com.belak.scheduletimetable.component.PresenceMapper;
import com.belak.scheduletimetable.component.StudentMapper;
import com.belak.scheduletimetable.dto.CreateStudentDto;
import com.belak.scheduletimetable.dto.PresenceDto;
import com.belak.scheduletimetable.dto.StudentDto;
import com.belak.scheduletimetable.enumeration.Filiere;

import com.belak.scheduletimetable.exception.ResourceNotFoundException;

import com.belak.scheduletimetable.model.Student;
import com.belak.scheduletimetable.repository.StudentRepository;
import lombok.RequiredArgsConstructor;

import org.apache.poi.ss.usermodel.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class StudentService {
    private  final StudentRepository studentRepository ;
    private  final StudentMapper studentMapper ;
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



}
