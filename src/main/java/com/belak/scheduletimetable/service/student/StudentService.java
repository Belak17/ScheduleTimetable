package com.belak.scheduletimetable.service.student;

import com.belak.scheduletimetable.component.StudentDetailsMapper;
import com.belak.scheduletimetable.component.StudentMapper;
import com.belak.scheduletimetable.component.StudentProfileMapper;
import com.belak.scheduletimetable.dto.student.StudentDetailsDto;
import com.belak.scheduletimetable.dto.student.StudentDto;
import com.belak.scheduletimetable.dto.student.StudentImportDto;
import com.belak.scheduletimetable.dto.student.StudentProfileDto;
import com.belak.scheduletimetable.enumeration.Filiere;

import com.belak.scheduletimetable.exception.ResourceNotFoundException;

import com.belak.scheduletimetable.model.Student;
import com.belak.scheduletimetable.repository.presence.PresenceRepository;
import com.belak.scheduletimetable.repository.student.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class StudentService implements  StudentInterfaceService {
    private  final StudentRepository studentRepository;
    private  final StudentMapper studentMapper ;
    private  final PresenceRepository presenceRepository ;
    private  final StudentProfileMapper profileMapper ;
    private  final PasswordEncoder passwordEncoder ;
    private  final StudentDetailsMapper studentDetailsMapper ;
    public Page<StudentDto> getStudentByFieldAndYearAndGroup(String field, String year, String groupe, int page, int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        String group = Filiere.fromCode(field).toString()+year;
        if (!groupe.startsWith("T"))
        {
            group = group+groupe;
        }
        else
        {
            group = group+" "+groupe;
        }
        return studentRepository.findByGroup(group,pageable).map(studentMapper::convertToDto);
    }

    public Page<StudentDto> getStudentByFieldAndYearAndGroup(String field,int year,String groupe,int page,int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        String group = Filiere.fromCode(field).getCode()+year;
        if (groupe==null)
        {
            return studentRepository.findByGroup(group,pageable).map(studentMapper::convertToDto);
        }
        else if (!groupe.startsWith("T"))
        {
            group = group+groupe;
        }
        else
        {
            group = group+" "+groupe;
        }
        return studentRepository.findByGroup(group,pageable).map(studentMapper::convertToDto);
    }
    public void saveStudent(StudentImportDto studentDto)
    {
        if (studentRepository.findByUserId(studentDto.getUserId()).isPresent())
        {
            throw new ResourceNotFoundException("Student already exists");
        }
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
                .orElseThrow(() -> new ResourceNotFoundException("Étudiant introuvable"));

        student.setEmail(nouvEmail);
        studentRepository.save(student);
    }

    public void updateInfo(String userId, String telephone) {
        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Étudiant introuvable"));
        student.setUserId(userId);

        student.setTelephone(telephone);

        studentRepository.save(student);
    }

    public void updatePassword(String oldPassword,
                               String newPassword,
                               String confPassword,
                               String userId) {

        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Étudiant introuvable"));

        if (!passwordEncoder.matches(oldPassword, student.getPassword())) {
            throw new IllegalArgumentException("Ancien mot de passe incorrect");
        }

        if (!newPassword.equals(confPassword)) {
            throw new IllegalArgumentException("Les mots de passe ne correspondent pas");
        }

        student.setPassword(passwordEncoder.encode(newPassword));
        studentRepository.save(student);
    }



    public Optional<Student> findStudentByUserId(String userId) {
        return studentRepository.findByUserId(userId);
    }

    public StudentDetailsDto getStudentDetailsDto(Long id)
    {
        return studentDetailsMapper.convertToStudentDetailsDto(studentRepository.findByIdUser(id));
    }

    @Override
    @Transactional
    public void updateStudent(Long id, StudentDetailsDto studentDto) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Étudiant introuvable avec l'identifiant : " + id
                ));

        // Vérification du niveau
        student.setNiveau(studentDto.getNiveau());

        // Informations personnelles
        student.setNom(studentDto.getNom());
        student.setPrenom(studentDto.getPrenom());
        student.setEmail(studentDto.getEmail());
        student.setTelephone(studentDto.getTelephone());
        student.setSexe(studentDto.getSexe());
        student.setNationalite(studentDto.getNationalite());

        // Informations académiques
        student.setDepartment(studentDto.getDepartment());
        student.setFiliere(studentDto.getFiliere());
        student.setNiveau(studentDto.getNiveau());
        student.setGroup(studentDto.getGroup());

        // Gestion du compte
        student.setEnabled(studentDto.isEnabled());

        studentRepository.save(student);
    }
}

