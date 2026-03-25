package com.belak.scheduletimetable.service.timetable.professortimetable;

import com.belak.scheduletimetable.dto.ProfessorDto;
import com.belak.scheduletimetable.dto.ProfessorTimetableDto;
import com.belak.scheduletimetable.enumeration.Grade;
import com.belak.scheduletimetable.enumeration.Statuts;
import com.belak.scheduletimetable.exception.EmptyFileException;
import com.belak.scheduletimetable.exception.InvalidExcelFormatException;
import com.belak.scheduletimetable.exception.LibreOfficeConversionException;
import com.belak.scheduletimetable.model.Professor;
import com.belak.scheduletimetable.model.ProfessorTimetable;
import com.belak.scheduletimetable.record.GroupInfo;
import com.belak.scheduletimetable.record.ProfessorData;
import com.belak.scheduletimetable.repository.ProfessorRepository;
import com.belak.scheduletimetable.repository.ProfessorTimetableRepository;
import com.belak.scheduletimetable.service.professor.ProfessorService;
import com.spire.xls.ExcelVersion;
import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfessorTimetableService {
    private final ProfessorTimetableRepository professorTimetableRepository ;
    private  final ProfessorRepository professorRepository ;
    private  final ProfessorService professorService ;
    public void sendManyProfessorTimetable(MultipartFile file) throws IOException, InterruptedException {
        validateFile(file);
        Workbook workbook = loadWorkbook(file);
        int sheetCount = workbook.getWorksheets().getCount();

        for (int i = 0; i < sheetCount; i++) {
            processSheet(workbook, i);
        }

        workbook.dispose();
    }
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new EmptyFileException("Le fichier Excel est vide");
        }
    }
    private Workbook loadWorkbook(MultipartFile file) throws IOException {
        Workbook workbook = new Workbook();
        workbook.loadFromStream(file.getInputStream());
        return workbook;
    }

    private void processSheet(Workbook originalWorkbook, int position) throws IOException, InterruptedException {
        Workbook singleWorkbook = new Workbook();

        try {
            Worksheet sourceSheet = originalWorkbook.getWorksheets().get(position);
            singleWorkbook.getWorksheets().clear();
            singleWorkbook.getWorksheets().addCopy(sourceSheet);

            Worksheet dataSheet = singleWorkbook.getWorksheets().get(0);

            ProfessorData info = extractProfessorInfo(dataSheet,position);
            adjustSheetLayout(dataSheet);

            File tempExcel = saveTempExcel(singleWorkbook, position);
            Path pdfPath = convertExcelToPdf(tempExcel, position);
            byte[] pdfBytes = Files.readAllBytes(pdfPath);

            saveProfessorTimetable(info, pdfBytes, position);

            cleanupFiles(tempExcel.toPath(), pdfPath);

        } finally {
            singleWorkbook.dispose();
        }
    }

    private ProfessorData extractProfessorInfo(Worksheet dataSheet, int position) {
        String fullname = dataSheet.getCellRange(4, 9).getValue();
        String grade = dataSheet.getCellRange(8, 7).getValue();
        String statut = dataSheet.getCellRange(10, 13).getValue();
        String speciality = dataSheet.getCellRange(11, 20).getValue();

        if (fullname == null || !fullname.contains(" ")) {
            throw new InvalidExcelFormatException("Nom complet manquant ou mal formaté à la sheet " + position);
        }

        String[] parts = fullname.trim().split("\\s+", 2);
        String nom = parts[0].trim();
        String prenom = parts.length > 1 ? parts[1].trim() : "";

        return new ProfessorData(prenom, nom, grade, statut, speciality);
    }

    private void adjustSheetLayout(Worksheet sheet) {
        for (int i = 8; i <= 16; i++) {
            sheet.setRowHeight(i, 40);
        }
        sheet.getPageSetup().setFitToPagesWide(1);
    }

    private File saveTempExcel(Workbook workbook, int position) throws IOException {
        String tempDir = System.getProperty("java.io.tmpdir");
        File tempExcel = new File(tempDir, "Timetable_" + position + ".xlsx");
        workbook.saveToFile(tempExcel.getAbsolutePath(), ExcelVersion.Version2013);
        return tempExcel;
    }

    private Path convertExcelToPdf(File tempExcel, int position) throws IOException, InterruptedException {
        String outputDir = System.getProperty("user.dir") + "/output";
        Files.createDirectories(Paths.get(outputDir));

        String libreOfficePath = "C:\\Program Files\\LibreOffice\\program\\soffice.exe";

        ProcessBuilder pb = new ProcessBuilder(
                libreOfficePath,
                "--headless",
                "--convert-to",
                "pdf",
                "--outdir",
                outputDir,
                tempExcel.getAbsolutePath()
        );
        pb.redirectErrorStream(true);

        Process process = pb.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new LibreOfficeConversionException("Erreur conversion PDF sheet " + position);
        }

        Path pdfPath = Paths.get(outputDir, tempExcel.getName().replace(".xlsx", ".pdf"));

        if (!Files.exists(pdfPath)) {
            throw new LibreOfficeConversionException("PDF non généré sheet " + position);
        }

        return pdfPath;
    }

    private void cleanupFiles(Path excelPath, Path pdfPath) throws IOException {
        Files.deleteIfExists(excelPath);
        Files.deleteIfExists(pdfPath);
    }

    private void saveProfessorTimetable(ProfessorData professorData, byte[] pdfBytes, int position) {
        Optional<Professor> optionalProfessor = professorRepository.findByNameNormalized(professorData.getPrenom(), professorData.getNom());
        optionalProfessor.ifPresent(professorEntity -> {
            ProfessorTimetable timetableEntity = ProfessorTimetable.builder()
                    .grade(Grade.fromCode(professorData.getGrade()))
                    .statut(Statuts.valueOf(professorData.getStatut().toUpperCase()))
                    .speciality(professorData.getSpeciality())
                    .position(position)
                    .filename("Timetable_" + position + ".pdf")
                    .contentType("application/pdf")
                    .fileData(pdfBytes)
                    .build();

            professorEntity.setTimetable(timetableEntity);
            timetableEntity.setProfessor(professorEntity);

            professorRepository.save(professorEntity);
        });
    }

    public Page<ProfessorTimetableDto> getProfessorTimetables(int page , int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return professorTimetableRepository.findAll(pageable).map(this::convertToDto);
    }

    public ProfessorTimetableDto convertToDto(ProfessorTimetable timetable)
    {
        ProfessorDto professorDto = professorService.convertToDto(timetable.getProfessor());
        return new ProfessorTimetableDto(timetable.getId(),
                timetable.getSpeciality(),
                timetable.getStatut().toString() , professorDto);
    }
}
