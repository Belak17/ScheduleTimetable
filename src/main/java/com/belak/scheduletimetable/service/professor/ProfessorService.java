package com.belak.scheduletimetable.service.professor;

import com.belak.scheduletimetable.component.ProfessorMapper;
import com.belak.scheduletimetable.dto.CreateProfessorDto;
import com.belak.scheduletimetable.dto.ProfessorDto;
import com.belak.scheduletimetable.enumeration.Nationalite;
import com.belak.scheduletimetable.exception.EmptyFileException;
import com.belak.scheduletimetable.exception.ResourceNotFoundException;
import com.belak.scheduletimetable.model.Professor;
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
public class ProfessorService  {
    private  final ProfessorRepository professorRepository ;
    private  final PasswordEncoder passwordEncoder ;
    private  final UtilsService utilsService ;
    private  final ProfessorMapper professorMapper ;



    public Page<ProfessorDto> getAllProfessor(int page , int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return professorRepository.findAll(pageable).map(professorMapper::convertToDto);
    }

    public void saveProfessor(CreateProfessorDto professorDto)
    {
        Professor professor = new Professor(professorDto);
        professorRepository.save(professor);
    }



    private Professor extractProfessor(Row row) {

        CreateProfessorDto dto = new CreateProfessorDto();

        dto.setPrenom(utilsService.getCellValue(row.getCell(0)));
        dto.setNom(utilsService.getCellValue(row.getCell(1)));
        dto.setPrenomArabe(utilsService.getCellValue(row.getCell(2)));
        dto.setNomArabe(utilsService.getCellValue(row.getCell(3)));
        dto.setCin(utilsService.getCellValue(row.getCell(4)));
        dto.setUserId(utilsService.getCellValue(row.getCell(5)));

        // password sécurisé
        String password = utilsService.getCellValue(row.getCell(6));
        dto.setPassword(password != null ? passwordEncoder.encode(password) : null);

        dto.setEmail(utilsService.getCellValue(row.getCell(7)));
        dto.setTelephone(utilsService.getCellValue(row.getCell(8)));
        dto.setSexe(utilsService.getCellValue(row.getCell(9)));

        dto.setDateNaissance(utilsService.parseDate(row.getCell(10)));

        dto.setVilleNaissance(utilsService.getCellValue(row.getCell(11)));
        dto.setAdresse(utilsService.getCellValue(row.getCell(12)));
        dto.setCodePostal(utilsService.getCellValue(row.getCell(13)));
        dto.setVille(utilsService.getCellValue(row.getCell(14)));

        // ENUM Nationalité
        dto.setNationalite(utilsService.parseNationalite(row.getCell(15)));

        // Département
        dto.setCodeDepartement(utilsService.getCellValue(row.getCell(16)));
        dto.setNomDepartement(utilsService.getCellValue(row.getCell(17)));

        // Grade
        dto.setCodeGrade(utilsService.getCellValue(row.getCell(18)));
        dto.setLibelleGrade(utilsService.getCellValue(row.getCell(19)));

        // Status
        dto.setCodeStatus(utilsService.getCellValue(row.getCell(20)));
        dto.setLibelleStatus(utilsService.getCellValue(row.getCell(21)));

        dto.setSpecialite(utilsService.getCellValue(row.getCell(22)));

        dto.setEtablissement_origine(utilsService.getCellValue(row.getCell(23)));
        dto.setRib(utilsService.getCellValue(row.getCell(24)));
        dto.setApplication_tiers(utilsService.getCellValue(row.getCell(25)));
        dto.setIdentifiantUnique(utilsService.getCellValue(row.getCell(26)));

        return new Professor(dto);
    }


//    private List<String> parseSpecialites(Cell cell) {
//        String value = getCellValue(cell);
//
//        if (value == null || value.isEmpty()) {
//            return new ArrayList<>();
//        }
//
//        return Arrays.stream(value.split(","))
//                .map(String::trim)
//                .filter(s -> !s.isEmpty())
//                .toList();
//    }

      public void updateProfessorPassword(UpdateRequest request)
      {
          Professor professor = professorRepository.findByUserId(request.getUserId());
          professor.setPassword(passwordEncoder.encode(request.getPassword()));
          professorRepository.save(professor);
      }
}
