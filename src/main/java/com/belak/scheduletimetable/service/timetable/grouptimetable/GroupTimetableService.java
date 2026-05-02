package com.belak.scheduletimetable.service.timetable.grouptimetable;

import com.belak.scheduletimetable.dto.GroupTimetableDto;
import com.belak.scheduletimetable.enumeration.Departement;
import com.belak.scheduletimetable.enumeration.Filiere;
import com.belak.scheduletimetable.exception.EmptyFileException;
import com.belak.scheduletimetable.exception.InvalidExcelFormatException;
import com.belak.scheduletimetable.exception.LibreOfficeConversionException;
import com.belak.scheduletimetable.exception.ResourceNotFoundException;
import com.belak.scheduletimetable.model.GroupTimetable;
import com.belak.scheduletimetable.model.Professor;
import com.belak.scheduletimetable.model.Student;
import com.belak.scheduletimetable.record.GroupInfo;
import com.belak.scheduletimetable.repository.GroupTimetableRepository;
import com.belak.scheduletimetable.repository.StudentRepository;
import com.belak.scheduletimetable.service.courstp.CoursTPService;
import com.spire.xls.ExcelVersion;
import com.spire.xls.Worksheet;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.spire.xls.Workbook;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class GroupTimetableService {
    private final GroupTimetableRepository groupTimetableRepository ;
    private final StudentRepository studentRepository ;
    private  final CoursTPService tpService ;
    public void sendManyGroupTimetable(MultipartFile file) throws IOException, InterruptedException {
        validateFile(file);

        Workbook originalWorkbook = loadWorkbook(file);

        try {
            int sheetNumbers = originalWorkbook.getWorksheets().getCount();

            for (int position = 0; position < sheetNumbers; position++) {
                processSheet(originalWorkbook, position);
            }
        } finally {
            originalWorkbook.dispose();
        }
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

            GroupInfo info = extractGroupInfo(dataSheet);
            adjustSheetLayout(dataSheet);


            File tempExcel = saveTempExcel(singleWorkbook, position);
            Path pdfPath = convertExcelToPdf(tempExcel, position);
            byte[] pdfBytes = Files.readAllBytes(pdfPath);

            GroupTimetable timetable = saveGroupTimetable(info, pdfBytes, position);

            tpService.extractAllCoursTPforOneGroup(dataSheet,timetable);

            cleanupFiles(tempExcel.toPath(), pdfPath);

        } finally {
            singleWorkbook.dispose();
        }
    }

    public String removeAccents(String text) {
        if (text == null) return null;

        return Normalizer
                .normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
    }

    public String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) return text;

        return text.substring(0, 1).toUpperCase() +
                text.substring(1).toLowerCase();
    }

    private GroupInfo extractGroupInfo(Worksheet sheet) {
        String department = sheet.getCellRange(1, 3).getValue();
        if (department == null || !department.contains(" ")) {
            throw new InvalidExcelFormatException("Department invalide");
        }
        department = department.trim();
        String[] parts = department.split(" ", 2);
        if (parts.length < 2) {
            throw new InvalidExcelFormatException("Format department incorrect");
        }
        String depname = parts[1].trim();
        depname =removeAccents(depname);
        depname = capitalizeFirstLetter(depname);

        String fullfield = sheet.getCellRange(6, 7).getValue();
        if (fullfield == null) {
            throw new InvalidExcelFormatException("Fullfield manquant");
        }
        fullfield = fullfield.trim();

        Pattern pattern = Pattern.compile("[123]");
        Matcher matcher = pattern.matcher(fullfield);
        if (!matcher.find()) {
            throw new InvalidExcelFormatException("Année introuvable");
        }

        int year = Integer.parseInt(matcher.group());
        int index = matcher.start();

        String field = fullfield.substring(0, index).trim();
        String group = fullfield.substring(index + 1).trim();

        if (group.endsWith(" .")) group = group.substring(0, group.length() - 2).trim();
        if (group.startsWith("-")) group = group.substring(1).trim();

        return new GroupInfo(depname, field, group, year);
    }

    private void adjustSheetLayout(Worksheet sheet) {
        for (int i = 8; i <= 16; i++) {
            sheet.setRowHeight(i, 40);
        }
        sheet.getPageSetup().setFitToPagesWide(1);
    }

    private File saveTempExcel(Workbook workbook, int position) throws IOException {
        String tempDir = System.getProperty("java.io.tmpdir");
        File tempExcel = new File(tempDir, "GroupTimetable_" + position + ".xlsx");
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

    private GroupTimetable saveGroupTimetable(GroupInfo info, byte[] pdfBytes, int position) {
        GroupTimetable entity = GroupTimetable.builder()
                .departement(Departement.fromLibelle(info.getDepname()))
                .position(position)
                .filename("GroupTimetable_" + position + ".pdf")
                .contentType("application/pdf")
                .fileData(pdfBytes)
                .niveau(info.getYear())
                .filiere(Filiere.fromCode(info.getField()))
                .group(info.getGroup())
                .build();

        List<Student> students = studentRepository.findByDepartmentAndFiliereAndNiveauAndGroup(
                Departement.fromLibelle(info.getDepname()),
                Filiere.fromCode(info.getField()),
                info.getYear(),
                info.getGroup()
        );

        for (Student student : students) {
            entity.addStudent(student);
        }

       return groupTimetableRepository.save(entity);
    }

    private void cleanupFiles(Path excelPath, Path pdfPath) throws IOException {
        Files.deleteIfExists(excelPath);
        Files.deleteIfExists(pdfPath);
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
    public Page<GroupTimetableDto> getGroupTimetables(int page , int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return groupTimetableRepository.findAll(pageable).map(this::convertToDto);
    }

    public GroupTimetableDto convertToDto(GroupTimetable groupTimetable) {
        return new GroupTimetableDto(
                groupTimetable.getId(),
                groupTimetable.getDepartement() != null ? groupTimetable.getDepartement().toString() : "NON DÉFINI",
                groupTimetable.getGroup(),
                groupTimetable.getFiliere() != null ? groupTimetable.getFiliere().toString() : "NON DÉFINI",
                groupTimetable.getNiveau()
        );
    }

    public byte[] getTimetablePreview(Long id ) throws IOException {
        GroupTimetable timetable = groupTimetableRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Professor non trouvé pour userId : " + id)
                );

        byte[] pdfBytes = timetable.getFileData();

        if (pdfBytes == null || pdfBytes.length == 0) {
            throw new ResourceNotFoundException("Le fichier PDF est vide");
        }

        return convertFirstPageToImage(pdfBytes);

    }

    public List<Filiere> getLicenceFilieresByDepartement(Departement departement) {
        return groupTimetableRepository
                .findDistinctFilieresByDepartement(departement)
                .stream()
                .filter(f -> f.name().startsWith("L"))
                .toList();
    }

    public List<Filiere> getMasterFilieresByDepartement(Departement departement) {
        return groupTimetableRepository
                .findDistinctFilieresByDepartement(departement)
                .stream()
                .filter(f -> f.name().startsWith("M"))
                .toList();
    }

    public List<Filiere> getFilieresByDepartement(Departement departement) {
        return groupTimetableRepository
                .findDistinctFilieresByDepartement(departement)
              ;
    }

}
