package com.belak.scheduletimetable.service.student;

import com.belak.scheduletimetable.dto.CreateStudentDto;
import com.belak.scheduletimetable.dto.StudentDto;
import com.belak.scheduletimetable.enumeration.Filiere;
import com.belak.scheduletimetable.enumeration.Nationalite;
import com.belak.scheduletimetable.enumeration.TypeDiplome;
import com.belak.scheduletimetable.exception.EmptyFileException;
import com.belak.scheduletimetable.exception.ResourceNotFoundException;
import com.belak.scheduletimetable.model.Student;
import com.belak.scheduletimetable.repository.StudentRepository;
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
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private  final StudentRepository studentRepository ;
    private  final PasswordEncoder passwordEncoder ;


    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new EmptyFileException("Le fichier Excel est vide");
        }
        String name = file.getOriginalFilename();
        if (name == null || !name.endsWith(".xlsx")) {
            throw new RuntimeException("Format invalide (.xlsx requis)");
        }
    }

    private XSSFWorkbook loadWorkbook(MultipartFile file) throws IOException {
        validateFile(file);
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
    public byte[] getTimetablePreview(String userId) throws IOException {
        Student student = studentRepository
                .findByUserIdWithTimetable(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Professor non trouvé pour userId : " + userId)
                );

        if (student.getGroupTimetable() == null  ) {
            throw new ResourceNotFoundException("Aucun emploi du temps associé au professeur");
        }

        byte[] pdfBytes = student.getGroupTimetable().getFileData();

        if (pdfBytes == null || pdfBytes.length == 0) {
            throw new ResourceNotFoundException("Le fichier PDF est vide");
        }

        return convertFirstPageToImage(pdfBytes);

    }


    public byte[] convertFirstPageToImage(byte[] pdfBytes) throws IOException {

        try (PDDocument document = PDDocument.load(pdfBytes)) {

            PDFRenderer pdfRenderer = new PDFRenderer(document);
            PDPage page = document.getPage(0);
            PDRectangle mediaBox = page.getMediaBox();

            float pdfHeight = mediaBox.getHeight();
            float targetHeight = 1000f; // plus grand que nécessaire
            float scale = targetHeight / pdfHeight;
            //BufferedImage image = pdfRenderer.renderImage(0, scale);
            BufferedImage image = pdfRenderer.renderImageWithDPI(0, 150);
            // 0 = première page

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);

            return baos.toByteArray();
        }
    }

    public Page<StudentDto> getStudentByFieldAndYearAndGroup(String field,int year,String group,int page,int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return studentRepository.findByFiliereAndNiveauAndGroup(Filiere.valueOf(field), year,group,pageable).map(this::convertToDto);
    }


    public StudentDto convertToDto(Student student)
    {
        return new StudentDto(student.getUserId(),
                student.getDepartment().toString()
                ,student.getFiliere().toString(),
                student.getGroup() ,
                student.getNiveau(),
                student.getNom(),
                student.getNom(),
                student.getEmail()
                );
    }

    public void saveStudent(CreateStudentDto studentDto)
    {
        Student student = new Student(studentDto);
        studentRepository.save(student);
    }

    private Student extractStudent(Row row) {

        CreateStudentDto dto = new CreateStudentDto();

        dto.setPrenom(getCellValue(row.getCell(0)));
        dto.setNom(getCellValue(row.getCell(1)));
        dto.setPrenomArabe(getCellValue(row.getCell(2)));
        dto.setNomArabe(getCellValue(row.getCell(3)));
        dto.setCin(getCellValue(row.getCell(4)));
        dto.setUserId(getCellValue(row.getCell(5)));

        String password = getCellValue(row.getCell(6));
        dto.setPassword(password != null ? passwordEncoder.encode(password) : null);

        dto.setEmail(getCellValue(row.getCell(7)));
        dto.setTelephone(getCellValue(row.getCell(8)));
        dto.setSexe(getCellValue(row.getCell(9)));

        dto.setDateNaissance(parseDate(row.getCell(10)));

        dto.setVilleNaissance(getCellValue(row.getCell(11)));
        dto.setVilleNaissanceArabe(getCellValue(row.getCell(12)));
        dto.setAdresse(getCellValue(row.getCell(13)));
        dto.setCodePostal(getCellValue(row.getCell(14)));
        dto.setVille(getCellValue(row.getCell(15)));

        dto.setNationalite(parseNationalite(row.getCell(16)));

        dto.setCodeDiplome(getCellValue(row.getCell(17)));
        dto.setNomDiplome(getCellValue(row.getCell(18)));
        dto.setTypeDiplome(parseTypeDiplome(row.getCell(19)));

        dto.setCodeDepartement(getCellValue(row.getCell(20)));
        dto.setNiveau(parseInteger(row.getCell(21)));
        dto.setNumeroInscription(getCellValue(row.getCell(22)));

        dto.setGroupeC(getCellValue(row.getCell(23)));
        dto.setGroupeTD(getCellValue(row.getCell(24)));
        dto.setGroupeM(getCellValue(row.getCell(25)));
        dto.setGroupeArchive(getCellValue(row.getCell(26)));

        return new Student(dto);
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return null;

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getLocalDateTimeCellValue().toLocalDate().toString();
                }
                yield String.valueOf((long) cell.getNumericCellValue());
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> null;
        };
    }
    private LocalDate parseDate(Cell cell) {
        if (cell == null) return null;

        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return cell.getLocalDateTimeCellValue().toLocalDate();
            }

            String value = cell.getStringCellValue().trim();

            DateTimeFormatter[] formats = {
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy")
            };

            for (DateTimeFormatter format : formats) {
                try {
                    return LocalDate.parse(value, format);
                } catch (Exception ignored) {}
            }

        } catch (Exception ignored) {}

        return null;
    }

    private Integer parseInteger(Cell cell) {
        if (cell == null) return null;

        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return (int) cell.getNumericCellValue();
            }

            return Integer.parseInt(cell.getStringCellValue().trim());

        } catch (Exception e) {
            return null;
        }
    }

    private Nationalite parseNationalite(Cell cell) {
        String value = getCellValue(cell);
        if (value == null) return null;

        return Nationalite.fromNationalite(value);
    }

    private TypeDiplome parseTypeDiplome(Cell cell) {
        String value = getCellValue(cell);
        if (value == null) return null;

        try {
            return TypeDiplome.valueOf(value.trim().toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }




}
