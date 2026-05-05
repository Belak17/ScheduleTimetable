package com.belak.scheduletimetable.service.timetable.professortimetable;

import com.belak.scheduletimetable.exception.ResourceNotFoundException;
import com.belak.scheduletimetable.model.Professor;
import com.belak.scheduletimetable.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProfessorPreviewService {
    private  final ProfessorRepository professorRepository ;
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

}
