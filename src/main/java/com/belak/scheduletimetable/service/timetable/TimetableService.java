package com.belak.scheduletimetable.service.timetable;

import com.belak.scheduletimetable.exception.EmptyFileException;
import com.belak.scheduletimetable.exception.LibreOfficeConversionException;
import com.spire.xls.Workbook;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class TimetableService {


    public Path convertExcelToPdf(File tempExcel, int position) throws IOException, InterruptedException {
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

    public void cleanupFiles(Path excelPath, Path pdfPath) throws IOException {
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
}
