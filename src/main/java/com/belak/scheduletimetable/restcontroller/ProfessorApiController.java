package com.belak.scheduletimetable.restcontroller;

import com.belak.scheduletimetable.dto.ProfessorDto;
import com.belak.scheduletimetable.request.UpdateRequest;
import com.belak.scheduletimetable.service.professor.ProfessorExcelService;
import com.belak.scheduletimetable.service.professor.ProfessorService;
import com.belak.scheduletimetable.service.timetable.grouptimetable.TimetablePreviewService;
import com.belak.scheduletimetable.service.timetable.professortimetable.ProfessorTimetableService;
import lombok.RequiredArgsConstructor;
import org.hibernate.dialect.function.PostgreSQLTruncRoundFunction;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import com.belak.scheduletimetable.enumeration.Departement;
@Controller
@RequestMapping("/professors")
@RequiredArgsConstructor
public class ProfessorApiController {
    private final TimetablePreviewService timetablePreviewService ;
    private  final ProfessorService professorService ;
    private  final ProfessorExcelService professorExcelService ;
    @GetMapping("/professor/timetable/{departement}")
    public String showProfessorTimetables(
            @PathVariable String departement ,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        Page<ProfessorDto> professors =
                professorService.getAllProfessorByDepartment(page, size, Departement.valueOf(departement));

        model.addAttribute("Professors", professors);
        model.addAttribute("selectedDepartment", departement);
        return "/admin/see-all-professor-timetable";
    }

    @GetMapping("/professor/timetable")
    public String showProfessorTimetables(


            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {


        model.addAttribute("Professors", null);
        return "/admin/see-all-professor-timetable";
    }

    @GetMapping("/preview/{userId}")
    public ResponseEntity<byte[]> getPreview(@PathVariable String userId ) throws IOException {

        byte[] image = timetablePreviewService
                .getTimetablePreview(userId);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(image);
    }

    @PostMapping("/import")
    public String uploadProfessors(@RequestParam("excelFile") MultipartFile file ,
                                 Model model ,
                                 RedirectAttributes redirectAttributes)  {
        try {
            professorExcelService.processSheet(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "admin/upload-users.html";
    }

    @PutMapping("/update")
    public  String updateProfessorPassword(@ModelAttribute UpdateRequest request)
    {
        professorService.updateProfessorPassword(request);
        return "";
    }
}
