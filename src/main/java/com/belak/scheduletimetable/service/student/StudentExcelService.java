package com.belak.scheduletimetable.service.student;

import com.belak.scheduletimetable.dto.CreateStudentDto;
import com.belak.scheduletimetable.enumeration.Filiere;
import com.belak.scheduletimetable.enumeration.Nationalite;
import com.belak.scheduletimetable.enumeration.TypeDiplome;
import com.belak.scheduletimetable.exception.EmptyFileException;
import com.belak.scheduletimetable.model.GroupTimetable;
import com.belak.scheduletimetable.model.Student;
import com.belak.scheduletimetable.repository.GroupTimetableRepository;
import com.belak.scheduletimetable.repository.StudentRepository;
import com.belak.scheduletimetable.service.OrthographyService;
import com.belak.scheduletimetable.service.UtilsService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.text.Normalizer;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentExcelService {
    private  final StudentRepository studentRepository ;
    private  final PasswordEncoder passwordEncoder ;
    private  final UtilsService utilsService ;
    private  final OrthographyService orthographyService ;
    private  final GroupTimetableRepository timetableRepository ;


    private XSSFWorkbook loadWorkbook(MultipartFile file) throws IOException {
        utilsService.validateFile(file);
        return new XSSFWorkbook(file.getInputStream());
    }


    @Transactional
    public void processSheet(MultipartFile file) throws IOException {

        List<Student> students = new ArrayList<>();

        try (XSSFWorkbook workbook = loadWorkbook(file)) {

            Sheet sheet = workbook.getSheetAt(0);

            System.out.println("========== DIAGNOSTIC EXCEL ==========");

            System.out.println("Nombre de lignes physiques : "
                    + sheet.getPhysicalNumberOfRows());

            System.out.println("Index de la dernière ligne : "
                    + sheet.getLastRowNum());

            int totalRows = 0;
            int skippedRows = 0;
            int extractedRows = 0;

            Iterator<Row> rows = sheet.iterator();

            // Ignorer l'en-tête
            if (rows.hasNext()) {
                rows.next();
            }

            while (rows.hasNext()) {

                Row row = rows.next();
                totalRows++;

                // Vérifier si la première cellule est absente
                if (row.getCell(0) == null) {

                    skippedRows++;

                    System.out.println(
                            "Ligne ignorée : " + (row.getRowNum() + 1)
                                    + " | Première cellule absente"
                    );

                    continue;
                }

                try {

                    Student student = extractStudent(row);
                    students.add(student);
                    extractedRows++;

                } catch (Exception e) {

                    System.err.println(
                            "Erreur à la ligne Excel : "
                                    + (row.getRowNum() + 1)
                    );

                    System.err.println("Message : " + e.getMessage());

                    e.printStackTrace();
                }
            }

            System.out.println("\n========== RÉSULTATS ==========");
            System.out.println("Lignes parcourues : " + totalRows);
            System.out.println("Lignes ignorées : " + skippedRows);
            System.out.println("Étudiants extraits : " + extractedRows);

        }

        System.out.println("\nÉtudiants à sauvegarder : " + students.size());

        studentRepository.saveAll(students);

        //addEachStudentToRelatedTimetable(students);


        System.out.println("Sauvegarde terminée.");
    }
    private Student extractStudent(Row row) throws IOException {

        CreateStudentDto dto = new CreateStudentDto();
        dto.setId(Long.parseLong(utilsService.getCellValue(row.getCell(0))));
        dto.setPrenom(utilsService.getCellValue(row.getCell(2)));
        dto.setNom(utilsService.getCellValue(row.getCell(1)));
        dto.setUserId(utilsService.getCellValue(row.getCell(5)));

        dto.setNumeroInscription( utilsService.getCellValue(row.getCell(6)));

        dto.setNationalite(utilsService.parseNationalite(row.getCell(7)));

        dto.setEmail(utilsService.getCellValue(row.getCell(9)));
        dto.setTelephone(utilsService.getCellValue(row.getCell(12)));
        dto.setSexe(utilsService.getCellValue(row.getCell(8)));

        dto.setDateNaissance(utilsService.parseDate(row.getCell(10)));

        dto.setFiliere(Filiere.fromLibelle(
                orthographyService.correctText(
                        utilsService.getCellValue(
                                (row.getCell(13))
                        )).toUpperCase()));

        dto.setNiveau(utilsService.getCellValue((row.getCell(14))));

        String group = utilsService.getCellValue(row.getCell(15));

// Extraire la partie avant le premier chiffre
        String fieldCode = group.replaceFirst("\\d.*", "");

// Normaliser le code de la filière
        String normalizedField = Filiere.fromCode(fieldCode).getCode();

// Remplacer le code initial par le code normalisé
        dto.setGroup(group.replaceFirst(
                "^" + java.util.regex.Pattern.quote(fieldCode),
                normalizedField
        ));

        return new Student(dto);
    }



    public static String removeAccents(String text) {
        return Normalizer
                .normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
    }

    public void  addStudentToGroupTimetable(Student student)
    {
        String fullfield = student.getGroup();
        // Extraire la partie avant le premier chiffre
        String fieldCode = fullfield.replaceFirst("\\d.*", "");

        // Normaliser le code de la filière
        String normalizedField = Filiere.fromCode(fieldCode).toString();

        // Remplacer le code initial par le code normalisé
        fullfield = fullfield.replaceFirst(
                "^" + java.util.regex.Pattern.quote(fieldCode),
                normalizedField);
        Optional< GroupTimetable> oGroupTimetable  = timetableRepository.findTimetable(fullfield);
        if (oGroupTimetable.isPresent())
            {
            GroupTimetable groupTimetable = oGroupTimetable.get();
            groupTimetable.addStudent(student);
            timetableRepository.save(groupTimetable);
            }

    }

    public void addEachStudentToRelatedTimetable(List<Student> students)
    {
        for (Student student : students)
        {
            addStudentToGroupTimetable(student);
        }
    }


}
