package com.belak.scheduletimetable.service.student;

import com.belak.scheduletimetable.dto.CreateStudentDto;
import com.belak.scheduletimetable.enumeration.Nationalite;
import com.belak.scheduletimetable.enumeration.TypeDiplome;
import com.belak.scheduletimetable.exception.EmptyFileException;
import com.belak.scheduletimetable.model.Student;
import com.belak.scheduletimetable.repository.StudentRepository;
import com.belak.scheduletimetable.service.UtilsService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentExcelService {
    private  final StudentRepository studentRepository ;
    private  final PasswordEncoder passwordEncoder ;
    private  final UtilsService utilsService ;


    private XSSFWorkbook loadWorkbook(MultipartFile file) throws IOException {
        utilsService.validateFile(file);
        return new XSSFWorkbook(file.getInputStream());
    }

    public  void processSheet(MultipartFile file) throws IOException {
        List<Student> students = new ArrayList<>();
        try {
            Sheet sheet;
            try (XSSFWorkbook workbook = loadWorkbook(file)) {
                sheet = workbook.getSheetAt(0);
            }
            Iterator<Row> rows = sheet.iterator();

            rows.next(); // header

            while (rows.hasNext()) {
                Row row = rows.next();

                if (row == null || row.getCell(0) == null) continue;
                students.add(extractStudent(row));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        studentRepository.saveAll(students);
    }
    private Student extractStudent(Row row) {

        CreateStudentDto dto = new CreateStudentDto();

        dto.setPrenom(utilsService.getCellValue(row.getCell(0)));
        dto.setNom(utilsService.getCellValue(row.getCell(1)));
        dto.setPrenomArabe(utilsService.getCellValue(row.getCell(2)));
        dto.setNomArabe(utilsService.getCellValue(row.getCell(3)));
        dto.setCin(utilsService.getCellValue(row.getCell(4)));
        dto.setUserId(utilsService.getCellValue(row.getCell(5)));

        String password = utilsService.getCellValue(row.getCell(6));
        dto.setPassword(password != null ? passwordEncoder.encode(password) : null);

        dto.setEmail(utilsService.getCellValue(row.getCell(7)));
        dto.setTelephone(utilsService.getCellValue(row.getCell(8)));
        dto.setSexe(utilsService.getCellValue(row.getCell(9)));

        dto.setDateNaissance(utilsService.parseDate(row.getCell(10)));

        dto.setVilleNaissance(utilsService.getCellValue(row.getCell(11)));
        dto.setVilleNaissanceArabe(utilsService.getCellValue(row.getCell(12)));
        dto.setAdresse(utilsService.getCellValue(row.getCell(13)));
        dto.setCodePostal(utilsService.getCellValue(row.getCell(14)));
        dto.setVille(utilsService.getCellValue(row.getCell(15)));

        dto.setNationalite(utilsService.parseNationalite(row.getCell(16)));

        dto.setCodeDiplome(utilsService.getCellValue(row.getCell(17)));
        dto.setNomDiplome(utilsService.getCellValue(row.getCell(18)));
        dto.setTypeDiplome(utilsService.parseTypeDiplome(row.getCell(19)));

        dto.setCodeDepartement(utilsService.getCellValue(row.getCell(20)));
        dto.setNiveau(utilsService.parseInteger(row.getCell(21)));
        dto.setNumeroInscription(utilsService.getCellValue(row.getCell(22)));

        dto.setGroupeC(utilsService.getCellValue(row.getCell(23)));
        dto.setGroupeTD(utilsService.getCellValue(row.getCell(24)));
        dto.setGroupeM(utilsService.getCellValue(row.getCell(25)));
        dto.setGroupeArchive(utilsService.getCellValue(row.getCell(26)));

        return new Student(dto);
    }


}
