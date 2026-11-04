package com.belak.scheduletimetable.service.professor;

import com.belak.scheduletimetable.dto.CreateProfessorDto;
import com.belak.scheduletimetable.enumeration.Grade;
import com.belak.scheduletimetable.model.CoursTP;
import com.belak.scheduletimetable.model.Professor;
import com.belak.scheduletimetable.repository.CoursTPRepository;
import com.belak.scheduletimetable.repository.ProfessorRepository;
import com.belak.scheduletimetable.service.UtilsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.text.WordUtils;
@Service
@RequiredArgsConstructor
@Slf4j
public class ProfessorExcelService {
    private  final UtilsService utilsService ;
    private  final ProfessorRepository professorRepository ;
    private  final PasswordEncoder passwordEncoder ;
    private  final CoursTPRepository tpRepository ;
    public XSSFWorkbook loadWorkbookForParse(MultipartFile file) throws IOException {
        utilsService.validateFile(file);
        return new XSSFWorkbook(file.getInputStream());
    }
    public  void processSheet(MultipartFile file) throws IOException {
//        Map<String, CoursTP> coursTPMap = tpRepository.findAll()
//                .stream()
//                .collect(Collectors.toMap(
//                        CoursTP::getIntitule,
//                        Function.identity()
//                ));

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
                Professor professor = extractProfessor(row);

                log.info(
                        "[EXCEL PROFESSOR] Ligne Excel : {} | ID : {} | Prénom : '{}' | Nom : '{}' | Nom complet : '{}' | Département : '{}' | Grade : '{}'",
                        row.getRowNum() + 1,
                        professor.getId(),
                        professor.getPrenom(),
                        professor.getNom(),
                        professor.getPrenom() + " " + professor.getNom(),
                        professor.getNomDepartement(),
                        professor.getGrade()
                );

                professors.add(
                        professor
                // assignCoursTPToProfessor(professor, coursTPMap)
                );
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        professorRepository.saveAll(professors);
    }

    private Professor extractProfessor(Row row) {

        CreateProfessorDto dto = new CreateProfessorDto();

        String fullname = utilsService.getCellValue(row.getCell(0));
        String[] parts = fullname.trim().split("\\s+", 2);
        dto.setPrenom(capitalize(parts[0].trim()));
        dto.setNom(capitalize(parts[1].trim()));

        dto.setUserId(utilsService.getCellValue(row.getCell(3)));

        dto.setGrade(Grade.fromLibelle(utilsService.getCellValue(row.getCell(1))));

        // password sécurisé
//        String password = utilsService.getCellValue(row.getCell(6));
//        dto.setPassword(password != null ? passwordEncoder.encode(password) : null);
//
        dto.setEmail(utilsService.getCellValue(row.getCell(3)));
//        dto.setTelephone(utilsService.getCellValue(row.getCell(8)));
//        dto.setSexe(utilsService.getCellValue(row.getCell(9)));
//
//        dto.setDateNaissance(utilsService.parseDate(row.getCell(10)));
//
//        // ENUM Nationalité
//        dto.setNationalite(utilsService.parseNationalite(row.getCell(15)));

        dto.setNomDepartement(utilsService.getCellValue(row.getCell(2)));

        // Grade
//        dto.setCodeGrade(utilsService.getCellValue(row.getCell(18)));
//        dto.setLibelleGrade(utilsService.getCellValue(row.getCell(19)));
//
//        // Status
//        dto.setCodeStatus(utilsService.getCellValue(row.getCell(20)));
//        dto.setLibelleStatus(utilsService.getCellValue(row.getCell(21)));
//
//        dto.setSpecialite(utilsService.getCellValue(row.getCell(22)));
//
//        dto.setEtablissement_origine(utilsService.getCellValue(row.getCell(23)));
//        dto.setRib(utilsService.getCellValue(row.getCell(24)));
//        dto.setApplication_tiers(utilsService.getCellValue(row.getCell(25)));
//        dto.setIdentifiantUnique(utilsService.getCellValue(row.getCell(26)));



        return new Professor(dto);
    }

    private String capitalize(String value) {
        if (value == null || value.isBlank()) {
            return value;
        }

        value = value.trim().toLowerCase(Locale.FRENCH);

        return value.substring(0, 1).toUpperCase(Locale.FRENCH)
                + value.substring(1);
    }

    public Professor assignCoursTPToProfessor(
            Professor professor,
            Map<String, CoursTP> coursTPMap) {

        if (professor == null || coursTPMap == null || coursTPMap.isEmpty()) {
            return professor;
        }

        String professorName = normalizeName(
                professor.getPrenom() + " " + professor.getNom()
        );

        String[] nameParts = professorName.split("\\s+");

        int assignedCount = 0;

        for (Map.Entry<String, CoursTP> entry : coursTPMap.entrySet()) {

            String intitule = entry.getKey();
            CoursTP coursTP = entry.getValue();

            if (intitule == null || coursTP == null) {
                continue;
            }

            String normalizedTitle = normalizeName(intitule);

            Set<String> titleWords = new HashSet<>(
                    Arrays.asList(normalizedTitle.split("\\s+"))
            );

            boolean allWordsFound = Arrays.stream(nameParts)
                    .allMatch(titleWords::contains);

            if (allWordsFound) {

                coursTP.setProfessor(professor);
                assignedCount++;

                log.debug(
                        "Cours '{}' associé au professeur '{}'",
                        intitule,
                        professor.getPrenom() + " " + professor.getNom()
                );
            }
        }

        log.info(
                "{} cours associés au professeur '{}'",
                assignedCount,
                professor.getPrenom() + " " + professor.getNom()
        );

        return professor;
    }

    private String normalizeName(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }

        return java.text.Normalizer.normalize(
                        value,
                        java.text.Normalizer.Form.NFD
                )
                .replaceAll("\\p{M}+", "")       // Supprime les accents
                .toLowerCase(Locale.ROOT)          // Ignore majuscules/minuscules
                .replaceAll("[^\\p{L}\\p{N}]+", " ") // Remplace la ponctuation par des espaces
                .trim()
                .replaceAll("\\s+", " ");         // Supprime les espaces multiples
    }



}
