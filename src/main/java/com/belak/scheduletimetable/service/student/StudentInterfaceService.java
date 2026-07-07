package com.belak.scheduletimetable.service.student;

import com.belak.scheduletimetable.dto.CreateStudentDto;
import com.belak.scheduletimetable.dto.StudentDto;
import com.belak.scheduletimetable.dto.StudentImportDto;
import com.belak.scheduletimetable.dto.StudentProfileDto;
import com.belak.scheduletimetable.model.Student;
import org.springframework.data.domain.Page;

public interface StudentInterfaceService {

    public Page<StudentDto> getStudentByFieldAndYearAndGroup(String field, int year, String group, int page, int size) ;

    public void saveStudent(StudentImportDto studentDto) ;

    public void deleteStudent(String userId) ;

    public StudentProfileDto findByUserId(String userId) ;

    public void updateEmail(String nouvEmail, String confEmail, String userId) ;

    public void updateInfo(String userId, String address, String telephone, String code) ;

    public void updatePassword(String oldPassword,
                               String newPassword,
                               String confPassword,
                               String userId) ;
    public Student findStudentByUserId(String userId) ;
}
