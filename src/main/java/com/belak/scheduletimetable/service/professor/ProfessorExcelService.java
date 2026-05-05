package com.belak.scheduletimetable.service.professor;

import com.belak.scheduletimetable.model.Professor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProfessorExcelService {
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

}
