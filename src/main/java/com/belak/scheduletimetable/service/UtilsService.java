package com.belak.scheduletimetable.service;

import com.belak.scheduletimetable.enumeration.Nationalite;
import com.belak.scheduletimetable.enumeration.TypeDiplome;
import com.belak.scheduletimetable.exception.EmptyFileException;
import com.spire.xls.Workbook;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class UtilsService {
    public String getCellValue(Cell cell) {
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
    public LocalDate parseDate(Cell cell) {
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

    public Integer parseInteger(Cell cell) {
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

    public Nationalite parseNationalite(Cell cell) {
        String value = getCellValue(cell);
        if (value == null) return null;

        return Nationalite.fromNationalite(value);
    }

    public TypeDiplome parseTypeDiplome(Cell cell) {
        String value = getCellValue(cell);
        if (value == null) return null;

        try {
            return TypeDiplome.valueOf(value.trim().toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    public void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new EmptyFileException("Le fichier Excel est vide");
        }
        String name = file.getOriginalFilename();
        if (name == null || !name.endsWith(".xlsx")) {
            throw new RuntimeException("Format invalide (.xlsx requis)");
        }
    }

    public Workbook loadWorkbook(MultipartFile file) throws IOException {
        Workbook workbook = new Workbook();
        workbook.loadFromStream(file.getInputStream());
        return workbook;
    }
}
