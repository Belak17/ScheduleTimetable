package com.belak.scheduletimetable.service.professor;

import com.belak.scheduletimetable.component.ProfessorMapper;
import com.belak.scheduletimetable.component.ProfessorProfileMapper;
import com.belak.scheduletimetable.dto.CreateProfessorDto;
import com.belak.scheduletimetable.dto.ProfessorDto;
import com.belak.scheduletimetable.dto.ProfessorProfileDto;
import com.belak.scheduletimetable.dto.StudentProfileDto;
import com.belak.scheduletimetable.enumeration.Departement;
import com.belak.scheduletimetable.enumeration.Nationalite;
import com.belak.scheduletimetable.exception.EmptyFileException;
import com.belak.scheduletimetable.exception.ResourceNotFoundException;
import com.belak.scheduletimetable.model.Professor;
import com.belak.scheduletimetable.model.User;
import com.belak.scheduletimetable.repository.ProfessorRepository;
import com.belak.scheduletimetable.request.UpdateRequest;
import com.belak.scheduletimetable.service.UtilsService;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Service
 @RequiredArgsConstructor
public class ProfessorService implements  ProfessorInterfaceService  {
    private  final ProfessorRepository professorRepository ;
    private  final PasswordEncoder passwordEncoder ;
    private  final UtilsService utilsService ;
    private  final ProfessorMapper professorMapper ;
    private  final ProfessorProfileMapper profileMapper ;

    public Page<ProfessorDto> getAllProfessor(int page , int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return professorRepository.findAll(pageable).map(professorMapper::convertToDto);
    }

    public Page<ProfessorDto> getAllProfessorByDepartment(int page , int size , Departement departement)
    {
        Pageable pageable = PageRequest.of(page, size);
        return professorRepository.findByDepartment(pageable, departement).map(professorMapper::convertToDto);
    }

    public void saveProfessor(CreateProfessorDto professorDto)
    {
        Professor professor = new Professor(professorDto);
        professorRepository.save(professor);
    }


      public void updateProfessorPassword(UpdateRequest request)
      {
          Professor professor = professorRepository.findByUserId(request.getUserId()).get();
          professor.setPassword(passwordEncoder.encode(request.getPassword()));
          professorRepository.save(professor);
      }
    public ProfessorProfileDto findByUserId(String userId)
    {
        return profileMapper.convertToProfessorProfileDto(professorRepository
                .findByUserId(userId).get());
    }

    public void updateEmail(String nouvEmail, String confEmail, String userId) {
        if (!nouvEmail.equals(confEmail)) {
            throw new IllegalArgumentException("Les emails ne correspondent pas");
        }

        Professor professor = professorRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Professeur introuvable"));

        professor.setEmail(nouvEmail);
        professorRepository.save(professor);
    }

    public void updateInfo(String userId, String address, String telephone, String code) {
        Professor professor = professorRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Professeur introuvable"));
        professor.setUserId(userId);
        professor.setAdresse(address);
        professor.setTelephone(telephone);
        professor.setCodePostal(code);
        professorRepository.save(professor);
    }

    public void updatePassword(String oldPassword,
                               String newPassword,
                               String confPassword,
                               String userId) {

        Professor professor = professorRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Professeur introuvable"));

        if (!passwordEncoder.matches(oldPassword, professor.getPassword())) {
            throw new IllegalArgumentException("Ancien mot de passe incorrect");
        }

        if (!newPassword.equals(confPassword)) {
            throw new IllegalArgumentException("Les mots de passe ne correspondent pas");
        }

        professor.setPassword(passwordEncoder.encode(newPassword));
        professorRepository.save(professor);
    }
}
