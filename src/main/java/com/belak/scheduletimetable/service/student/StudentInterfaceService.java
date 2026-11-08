package com.belak.scheduletimetable.service.student;

import com.belak.scheduletimetable.dto.student.StudentDetailsDto;
import com.belak.scheduletimetable.dto.student.StudentDto;
import com.belak.scheduletimetable.dto.student.StudentImportDto;
import com.belak.scheduletimetable.dto.student.StudentProfileDto;
import com.belak.scheduletimetable.model.Student;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface StudentInterfaceService {

    public Page<StudentDto> getStudentByFieldAndYearAndGroup(String field, String year, String group, int page, int size) ;

    public void saveStudent(StudentImportDto studentDto) ;

    public void deleteStudent(String userId) ;

    public StudentProfileDto findByUserId(String userId) ;

    public void updateEmail(String nouvEmail, String confEmail, String userId) ;

    public void updateInfo(String userId, String telephone) ;

    public void updatePassword(String oldPassword,
                               String newPassword,
                               String confPassword,
                               String userId) ;
    public Optional<Student> findStudentByUserId(String userId) ;

    @Nullable Object getStudentDetailsDto(Long id);

    void updateStudent(Long id, StudentDetailsDto studentDto);
}
