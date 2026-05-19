package com.belak.scheduletimetable.controller;

import com.belak.scheduletimetable.dto.GroupTimetableDto;
import com.belak.scheduletimetable.enumeration.Departement;
import com.belak.scheduletimetable.exception.EmptyFileException;
import com.belak.scheduletimetable.exception.InvalidExcelFormatException;
import com.belak.scheduletimetable.exception.LibreOfficeConversionException;
import com.belak.scheduletimetable.model.GroupTimetable ;
import com.belak.scheduletimetable.service.timetable.grouptimetable.GroupTimetableService;
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
@RequestMapping("/timetables")
@RequiredArgsConstructor
public class GroupTimetableController {
    private  final GroupTimetableService timetableService ;
    @PostMapping("/upload/grouptimetable")
    public String upload( @RequestParam("excelFile") MultipartFile file , Model model , RedirectAttributes redirectAttributes)  {

        try {
            timetableService.sendManyGroupTimetable(file);
            redirectAttributes.addFlashAttribute("success", "Upload terminé avec succès");
        } catch (EmptyFileException e) {
            redirectAttributes.addFlashAttribute("error", "Le fichier est vide !");
        } catch (InvalidExcelFormatException e) {
            redirectAttributes.addFlashAttribute("error", "Erreur de format dans le fichier Excel : " + e.getMessage());
        } catch (LibreOfficeConversionException e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la conversion PDF : " + e.getMessage());
        } catch ( IOException | InterruptedException e ) {
            redirectAttributes.addFlashAttribute("error", "Erreur inattendue lors du traitement du fichier : " + e.getMessage());
        }
        // Rediriger vers la page admin/timetable
        return "redirect:/admin/timetable";
    }

    @GetMapping("/group/timetable/{departement}")
    public String showGroupTimetables(
            @PathVariable String departement ,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {

        Page<GroupTimetableDto> timetables =
                timetableService.getGroupTimetablesByDepartment(
                        page,
                        size,
                        Departement.valueOf(departement)
                );

        model.addAttribute("GroupTimetableList", timetables);
        model.addAttribute("selectedDepartment", departement);

        return "/admin/see-all-group-timetable";
    }

    @GetMapping("/group/timetable")
    public String showGroupTimetables(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        model.addAttribute("GroupTimetableList", null);
        return "/admin/see-all-group-timetable";
    }

    @GetMapping("/preview/{id}")
    public ResponseEntity<byte[]> getPreview(@PathVariable Long id ) throws IOException {

        byte[] image = timetableService.getTimetablePreview(id);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(image);
    }




}
