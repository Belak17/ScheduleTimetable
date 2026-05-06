package com.belak.scheduletimetable.service.professor;

import com.belak.scheduletimetable.dto.CreateProfessorDto;
import com.belak.scheduletimetable.model.Professor;
import com.belak.scheduletimetable.repository.ProfessorRepository;
import com.belak.scheduletimetable.service.UtilsService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessorExcelService {
    private  final UtilsService utilsService ;
    private  final ProfessorRepository professorRepository ;
    private  final PasswordEncoder passwordEncoder ;
    public XSSFWorkbook loadWorkbookForParse(MultipartFile file) throws IOException {
        utilsService.validateFile(file);
        return new XSSFWorkbook(file.getInputStream());
    }
    public  void processSheet(MultipartFile file) throws IOException {
        List<Professor> professors = new ArrayList<>();
        try {
            Sheet sheet;
            try (XSSFWorkbook workbook = loadWorkbookForParse(file)) {
                sheet = workbook.getSheetAt(0);
            }
            Iterator<Row> rows = sheet.iterator();

            rows.next(); // header

            while (rows.hasNext()) {
                Row row = rows.next();

                if (row == null || row.getCell(0) == null) continue;
                professors.add(extractProfessor(row));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        professorRepository.saveAll(professors);
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

}
