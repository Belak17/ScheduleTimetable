package com.belak.scheduletimetable.restcontroller;

import com.belak.scheduletimetable.dto.StudentDto;
import com.belak.scheduletimetable.model.Student;
import com.belak.scheduletimetable.service.student.StudentService;
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

@Controller
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentApiController {

    private  final StudentService studentService ;

    @GetMapping("/{field}/{year}/{group}/details")
    public String showGroupStudents( Model model,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @PathVariable String field,
                                    @PathVariable String group,
                                    @PathVariable int year
    )
    {
        Page<StudentDto> students = studentService.getStudentByFieldAndYearAndGroup(field,year,group,page,size);
        model.addAttribute("students", students);
        model.addAttribute("field", field);
        model.addAttribute("group", group);
        model.addAttribute("year", year);
        return "admin/see-group-student.html";
    }

    @GetMapping("/preview/{userId}")
    public ResponseEntity<byte[]> getPreview(@PathVariable String userId ) throws IOException {

        byte[] image = studentService
                .getTimetablePreview(userId);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(image);
    }


    @PostMapping("/import")
    public String uploadStudents(@RequestParam("excelFile") MultipartFile file ,
                                 Model model ,
                                 RedirectAttributes redirectAttributes)  {
        try {
            studentService.processSheet(file);
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
