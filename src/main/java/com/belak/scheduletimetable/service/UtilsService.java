package com.belak.scheduletimetable.service;

import com.belak.scheduletimetable.enumeration.Nationalite;
import com.belak.scheduletimetable.enumeration.TypeDiplome;
import com.belak.scheduletimetable.exception.EmptyFileException;
import com.spire.xls.Workbook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UtilsService {

    public String getCellValue(Cell cell) {
        if (cell == null) return null;

        return switch (cell.getCellType()) {
            case STRING -> {
                String value = cell.getStringCellValue();
                yield value != null ? value.trim() : null;
            }

            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getLocalDateTimeCellValue().toLocalDate().toString().trim();
                }
                yield String.valueOf((long) cell.getNumericCellValue()).trim();
            }

            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue()).trim();

            default -> null;
        };
    }

    public LocalDate parseDate(Cell cell) {
        if (cell == null) return null;

        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return cell.getLocalDateTimeCellValue().toLocalDate();
            }

            String value = cell.getStringCellValue();

            if (value == null || value.trim().isEmpty()) {
                return null;
            }

            value = value.trim();

            DateTimeFormatter[] formats = {
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy")
            };

            for (DateTimeFormatter format : formats) {
                try {
                    return LocalDate.parse(value.trim(), format);
                } catch (Exception ignored) {}
            }

        } catch (Exception ignored) {}

        return null;
    }

    public Integer parseInteger(Cell cell) {
        if (cell == null) return null;

        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return (int) cell.getNumericCellValue();
            }

            String value = cell.getStringCellValue();

            if (value == null || value.trim().isEmpty()) {
                return null;
            }

            return Integer.parseInt(value.trim());

        } catch (Exception e) {
            return null;
        }
    }

    public Nationalite parseNationalite(Cell cell) {
        String value = getCellValue(cell);

        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        return Nationalite.fromNationalite(value.trim());
    }

    public TypeDiplome parseTypeDiplome(Cell cell) {
        String value = getCellValue(cell);

        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            return TypeDiplome.valueOf(value.trim().toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    public void validateFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new EmptyFileException("Le fichier Excel est vide");
        }

        String name = file.getOriginalFilename();

        if (name == null || !name.trim().toLowerCase().endsWith(".xlsx")) {
            log.info("Le fichier reçu est invalide : Mauvais format de fichier");
            throw new RuntimeException("Format invalide (.xlsx requis)");
        }
    }

    public Workbook loadWorkbook(MultipartFile file) throws IOException {
        Workbook workbook = new Workbook();
        workbook.loadFromStream(file.getInputStream());
        return workbook;
    }

    private List<String> parseSpecialites(Cell cell) {
        String value = getCellValue(cell);

        if (value == null || value.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}