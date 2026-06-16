package com.belak.scheduletimetable.restcontroller;

import com.belak.scheduletimetable.dto.StudentDto;
import com.belak.scheduletimetable.enumeration.Filiere;
import com.belak.scheduletimetable.enumeration.Semester;
import com.belak.scheduletimetable.model.Student;
import com.belak.scheduletimetable.service.student.StudentExcelService;
import com.belak.scheduletimetable.service.student.StudentService;
import com.belak.scheduletimetable.service.timetable.grouptimetable.TimetablePreviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentApiController {
    private  final StudentExcelService studentExcelService ;
    private  final StudentService studentService ;
    private  final TimetablePreviewService preview ;

    @GetMapping("/{field}/{year}/{group}/details")
    public String showGroupStudents( Model model,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @PathVariable String field,
                                    @PathVariable String group,
                                    @PathVariable int year
    )
    {
        Page<StudentDto> students = studentService.getStudentByFieldAndYearAndGroup(Filiere.fromCode(field).toString(),year,group,page,size);
        model.addAttribute("students", students);
        model.addAttribute("field", field);
        model.addAttribute("group", group);
        model.addAttribute("year", year);
        return "admin/see-group-student.html";
    }

    @GetMapping("/preview/{userId}")
    public ResponseEntity<byte[]> getPreview(@PathVariable String userId , @RequestParam(required = false) Semester semester ) throws IOException {

        Semester effectiveSemester =
                (semester != null) ? semester : Semester.fromDate(LocalDate.now());
        byte[] image = preview
                .getTimetablePreview(userId ,effectiveSemester);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(image);
    }


    @PostMapping("/import")
    public String uploadStudents(@RequestParam("excelFile") MultipartFile file ,
                                 Model model ,
                                 RedirectAttributes redirectAttributes)  {
        try {
            studentExcelService.processSheet(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "admin/upload-users.html";
    }
    @DeleteMapping("/delete/{userId}/student")
    public String deleteStudent(@PathVariable String userId)
    {
        studentService.deleteStudent(userId);
        return "";
    }


}
