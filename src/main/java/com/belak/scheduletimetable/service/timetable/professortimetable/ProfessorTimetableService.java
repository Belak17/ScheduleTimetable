package com.belak.scheduletimetable.service.timetable.professortimetable;

import com.belak.scheduletimetable.component.ProfessorTimetableMapper;
import com.belak.scheduletimetable.dto.ProfessorTimetableDto;
import com.belak.scheduletimetable.enumeration.Grade;
import com.belak.scheduletimetable.enumeration.Statuts;
import com.belak.scheduletimetable.exception.InvalidExcelFormatException;
import com.belak.scheduletimetable.model.Professor;
import com.belak.scheduletimetable.model.ProfessorTimetable;
import com.belak.scheduletimetable.record.ProfessorData;
import com.belak.scheduletimetable.repository.ProfessorRepository;
import com.belak.scheduletimetable.repository.ProfessorTimetableRepository;
import com.belak.scheduletimetable.service.UtilsService;
import com.belak.scheduletimetable.service.timetable.TimetableService;
import com.spire.xls.ExcelVersion;
import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfessorTimetableService extends TimetableService {

    private final ProfessorTimetableRepository professorTimetableRepository;
    private final ProfessorRepository professorRepository;
    private final ProfessorTimetableMapper timetableMapper;
    private final UtilsService utilsService;

    /**
     * Importation des emplois du temps de plusieurs professeurs.
     */
    public void sendManyProfessorTimetable(MultipartFile file)
            throws IOException, InterruptedException {

        utilsService.validateFile(file);

        Workbook workbook = utilsService.loadWorkbook(file);

        try {
            int sheetCount = workbook.getWorksheets().getCount();

            log.info("Début de l'importation des emplois du temps. Nombre de feuilles : {}",
                    sheetCount);

            for (int i = 0; i < sheetCount; i++) {

                try {
                    processSheet(workbook, i);

                } catch (Exception e) {
                    log.error("Échec de l'importation de la feuille Excel numéro {} : {}",
                            i, e.getMessage(), e);

                    throw e;
                }
            }

            log.info("Importation terminée avec succès. {} feuilles traitées.",
                    sheetCount);

        } finally {
            workbook.dispose();
            log.debug("Workbook Excel original libéré.");
        }
    }

    /**
     * Traitement individuel d'une feuille Excel.
     */
    private void processSheet(Workbook originalWorkbook, int position)
            throws IOException, InterruptedException {

        Workbook singleWorkbook = new Workbook();

        Path tempExcel = null;
        Path pdfPath = null;

        try {
            log.info("Traitement de la feuille numéro {}", position);

            Worksheet sourceSheet =
                    originalWorkbook.getWorksheets().get(position);

            singleWorkbook.getWorksheets().clear();
            singleWorkbook.getWorksheets().addCopy(sourceSheet);

            Worksheet dataSheet = singleWorkbook.getWorksheets().get(0);

            ProfessorData info = extractProfessorInfo(dataSheet, position);

            log.debug("Professeur détecté : {} {}",
                    info.getPrenom(), info.getNom());

            adjustSheetLayout(dataSheet);

            tempExcel = saveTempExcel(singleWorkbook, position);

            log.debug("Fichier Excel temporaire créé : {}", tempExcel);

            pdfPath = convertExcelToPdf(tempExcel.toFile(), position);

            log.debug("Conversion PDF terminée : {}", pdfPath);

            byte[] pdfBytes = Files.readAllBytes(pdfPath);

            log.debug("Taille du PDF généré : {} octets", pdfBytes.length);

            saveProfessorTimetable(info, pdfBytes, position);

            log.info("Traitement de la feuille {} terminé.", position);

        } finally {

            // Nettoyage des fichiers temporaires
            if (tempExcel != null || pdfPath != null) {

                try {
                    cleanupFiles(tempExcel, pdfPath);

                } catch (Exception e) {
                    log.warn("Impossible de nettoyer les fichiers temporaires de la feuille {}",
                            position, e);
                }
            }

            singleWorkbook.dispose();

            log.debug("Ressources de la feuille {} libérées.", position);
        }
    }

    /**
     * Extraction des informations du professeur.
     */
    private ProfessorData extractProfessorInfo(Worksheet dataSheet, int position) {

        String fullname = dataSheet.getCellRange(4, 9).getValue();
        String grade = dataSheet.getCellRange(8, 7).getValue();
        String statut = dataSheet.getCellRange(10, 13).getValue();
        String speciality = dataSheet.getCellRange(11, 20).getValue();

        if (fullname == null || fullname.isBlank() || !fullname.trim().contains(" ")) {
//            throw new InvalidExcelFormatException(
//                    "Nom complet manquant ou mal formaté à la feuille " + position
//            );
            fullname = "Not Available";
        }

        String[] parts = fullname.trim().split("\\s+", 2);

        String prenom= capitalize(parts[0].trim());
        String nom = capitalize(parts[1].trim());

        if (grade == null || grade.isBlank()) {
            grade = "";
        }

        if (statut == null || statut.isBlank()) {
                statut = ""  ;
        }

        if (speciality == null || speciality.isBlank()) {
            speciality = ""  ;
            log.warn("Spécialité absente à la feuille {}", position);
        }

        return new ProfessorData(
                prenom,
                nom,
                grade.trim(),
                statut.trim(),
                speciality
        );
    }

    /**
     * Ajustement de la mise en page Excel.
     */
    private void adjustSheetLayout(Worksheet sheet) {

        for (int i = 8; i <= 16; i++) {
            sheet.setRowHeight(i, 40);
        }

        sheet.getPageSetup().setFitToPagesWide(1);
    }

    /**
     * Création d'un fichier Excel temporaire.
     */
    private Path saveTempExcel(Workbook workbook, int position) throws IOException {

        Path tempExcel = Files.createTempFile(
                "Timetable_" + position + "_",
                ".xlsx"
        );

        workbook.saveToFile(
                tempExcel.toAbsolutePath().toString(),
                ExcelVersion.Version2013
        );

        return tempExcel;
    }

    /**
     * Enregistrement de l'emploi du temps du professeur.
     */
    @Transactional
    public void saveProfessorTimetable(
            ProfessorData professorData,
            byte[] pdfBytes,
            int position
    ) {

        Optional<Professor> optionalProfessor =
                professorRepository.findByNameNormalized(
                        professorData.getPrenom(),
                        professorData.getNom()
                );

        if (optionalProfessor.isEmpty()) {

            log.warn("Professeur introuvable en base : {} {}. Feuille : {}",
                    professorData.getPrenom(),
                    professorData.getNom(),
                    position);

            return;
        }

        Professor professor = optionalProfessor.get();

        Grade grade = Grade.fromCode(professorData.getGrade());

        if (grade == null) {
                grade = Grade.AUTRE ;
        }

        Statuts statut;

        try {
            statut = Statuts.valueOf(
                    professorData.getStatut().trim().toUpperCase()
            );

        } catch (IllegalArgumentException e) {
            statut = Statuts.AUTRE;
        }

        ProfessorTimetable timetableEntity = ProfessorTimetable.builder()
                .grade(grade)
                .statut(statut)
                .speciality(professorData.getSpeciality())
                .position(position)
                .filename("Timetable_" + position + ".pdf")
                .contentType("application/pdf")
                .fileData(pdfBytes)
                .build();

        professor.setTimetable(timetableEntity);
        timetableEntity.setProfessor(professor);

        professorRepository.save(professor);

        log.info("Emploi du temps enregistré pour {} {}.",
                professorData.getNom(),
                professorData.getPrenom());
    }

    /**
     * Récupération paginée des emplois du temps.
     */
    @Transactional(readOnly = true)
    public Page<ProfessorTimetableDto> getProfessorTimetables(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return professorTimetableRepository
                .findAll(pageable)
                .map(timetableMapper::convertToDto);
    }
    private String capitalize(String value) {
        if (value == null || value.isBlank()) {
            return value;
        }

        value = value.trim().toLowerCase(Locale.FRENCH);

        return value.substring(0, 1).toUpperCase(Locale.FRENCH)
                + value.substring(1);
    }
}