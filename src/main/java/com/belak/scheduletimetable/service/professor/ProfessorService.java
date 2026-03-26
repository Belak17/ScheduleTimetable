package com.belak.scheduletimetable.service.professor;

import com.belak.scheduletimetable.dto.CreateProfessorDto;
import com.belak.scheduletimetable.dto.CreateStudentDto;
import com.belak.scheduletimetable.dto.ProfessorDto;
import com.belak.scheduletimetable.dto.ProfessorTimetableDto;
import com.belak.scheduletimetable.exception.ResourceNotFoundException;
import com.belak.scheduletimetable.model.Professor;
import com.belak.scheduletimetable.model.Student;
import com.belak.scheduletimetable.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
 @RequiredArgsConstructor
public class ProfessorService {
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
}
