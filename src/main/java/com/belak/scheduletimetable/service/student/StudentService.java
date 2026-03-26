package com.belak.scheduletimetable.service.student;

import com.belak.scheduletimetable.dto.CreateStudentDto;
import com.belak.scheduletimetable.dto.ProfessorDto;
import com.belak.scheduletimetable.dto.StudentDto;
import com.belak.scheduletimetable.enumeration.Filiere;
import com.belak.scheduletimetable.exception.ResourceNotFoundException;
import com.belak.scheduletimetable.model.Professor;
import com.belak.scheduletimetable.model.Student;
import com.belak.scheduletimetable.repository.StudentRepository;
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
public class StudentService {
    private  final StudentRepository studentRepository ;

    public byte[] getTimetablePreview(String userId) throws IOException {
        Student student = studentRepository
                .findByUserIdWithTimetable(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Professor non trouvé pour userId : " + userId)
                );

        if (student.getGroupTimetable() == null  ) {
            throw new ResourceNotFoundException("Aucun emploi du temps associé au professeur");
        }

        byte[] pdfBytes = student.getGroupTimetable().getFileData();

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

    public Page<StudentDto> getStudentByFieldAndYearAndGroup(String field,int year,String group,int page,int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return studentRepository.findByFiliereAndNiveauAndGroup(Filiere.valueOf(field), year,group,pageable).map(this::convertToDto);
    }


    public StudentDto convertToDto(Student student)
    {
        return new StudentDto(student.getUserId(),
                student.getDepartment().toString()
                ,student.getFiliere().toString(),
                student.getGroup() ,
                student.getNiveau(),
                student.getNom(),
                student.getNom(),
                student.getEmail()
                );
    }

    public void saveStudent(CreateStudentDto studentDto)
    {
        Student student = new Student(studentDto);
        studentRepository.save(student);
    }
}
