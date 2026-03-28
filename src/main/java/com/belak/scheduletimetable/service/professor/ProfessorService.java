package com.belak.scheduletimetable.service.professor;

import com.belak.scheduletimetable.dto.CreateProfessorDto;
import com.belak.scheduletimetable.dto.ProfessorDto;
import com.belak.scheduletimetable.enumeration.Nationalite;
import com.belak.scheduletimetable.exception.EmptyFileException;
import com.belak.scheduletimetable.exception.ResourceNotFoundException;
import com.belak.scheduletimetable.model.Professor;
import com.belak.scheduletimetable.repository.ProfessorRepository;
import com.belak.scheduletimetable.request.UpdateRequest;
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
public class ProfessorService {
    private  final ProfessorRepository professorRepository ;
    private  final PasswordEncoder passwordEncoder ;
    public byte[] getTimetablePreview(String userId) throws IOException {
        Professor professor = professorRepository
                .findByUserIdWithTimetable(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Professor non trouvé pour userId : " + userId)
                );

        if (professor.getTimetable() == null  ) {
            throw new ResourceNotFoundException("Aucun emploi du temps associé au professeur");
        }

        byte[] pdfBytes = professor.getTimetable().getFileData();

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

    public ProfessorDto convertToDto(Professor professor)
    {
        return new ProfessorDto(professor.getUserId(),
                professor.getNom()
                ,professor.getPrenom(),
                professor.getGrade().toString()
                ,professor.getSchoolStatus().toString(),
                professor.getSpecialite());
    }

    public Page<ProfessorDto> getAllProfessor(int page , int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return professorRepository.findAll(pageable).map(this::convertToDto);
    }

    public void saveProfessor(CreateProfessorDto professorDto)
    {
        Professor professor = new Professor(professorDto);
        professorRepository.save(professor);
    }

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
        List<Professor> professors = new ArrayList<>();
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
                professors.add(extractProfessor(row));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        professorRepository.saveAll(professors);
    }


    private Professor extractProfessor(Row row) {

        CreateProfessorDto dto = new CreateProfessorDto();

        dto.setPrenom(getCellValue(row.getCell(0)));
        dto.setNom(getCellValue(row.getCell(1)));
        dto.setPrenomArabe(getCellValue(row.getCell(2)));
        dto.setNomArabe(getCellValue(row.getCell(3)));
        dto.setCin(getCellValue(row.getCell(4)));
        dto.setUserId(getCellValue(row.getCell(5)));

        // password sécurisé
        String password = getCellValue(row.getCell(6));
        dto.setPassword(password != null ? passwordEncoder.encode(password) : null);

        dto.setEmail(getCellValue(row.getCell(7)));
        dto.setTelephone(getCellValue(row.getCell(8)));
        dto.setSexe(getCellValue(row.getCell(9)));

        dto.setDateNaissance(parseDate(row.getCell(10)));

        dto.setVilleNaissance(getCellValue(row.getCell(11)));
        dto.setAdresse(getCellValue(row.getCell(12)));
        dto.setCodePostal(getCellValue(row.getCell(13)));
        dto.setVille(getCellValue(row.getCell(14)));

        // ENUM Nationalité
        dto.setNationalite(parseNationalite(row.getCell(15)));

        // Département
        dto.setCodeDepartement(getCellValue(row.getCell(16)));
        dto.setNomDepartement(getCellValue(row.getCell(17)));

        // Grade
        dto.setCodeGrade(getCellValue(row.getCell(18)));
        dto.setLibelleGrade(getCellValue(row.getCell(19)));

        // Status
        dto.setCodeStatus(getCellValue(row.getCell(20)));
        dto.setLibelleStatus(getCellValue(row.getCell(21)));

        dto.setSpecialite(getCellValue(row.getCell(22)));

        dto.setEtablissement_origine(getCellValue(row.getCell(23)));
        dto.setRib(getCellValue(row.getCell(24)));
        dto.setApplication_tiers(getCellValue(row.getCell(25)));
        dto.setIdentifiantUnique(getCellValue(row.getCell(26)));

        return new Professor(dto);
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
    private Nationalite parseNationalite(Cell cell) {
        String value = getCellValue(cell);
        if (value == null) return null;

        return Nationalite.fromNationalite(value);
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
