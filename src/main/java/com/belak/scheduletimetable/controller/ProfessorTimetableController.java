package com.belak.scheduletimetable.controller;

import com.belak.scheduletimetable.dto.ProfessorTimetableDto;
import com.belak.scheduletimetable.exception.EmptyFileException;
import com.belak.scheduletimetable.exception.InvalidExcelFormatException;
import com.belak.scheduletimetable.exception.LibreOfficeConversionException;
import com.belak.scheduletimetable.model.ProfessorTimetable;
import com.belak.scheduletimetable.service.timetable.professortimetable.ProfessorTimetableService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/timetables")
public class ProfessorTimetableController {
    private  final ProfessorTimetableService timetableService ;

    @PostMapping("/upload/professortimetable")
    public String upload( @RequestParam("excelFile") MultipartFile file ,
                          Model model ,
                          RedirectAttributes redirectAttributes)  {

        try {
            timetableService.sendManyProfessorTimetable(file);
            redirectAttributes.addFlashAttribute("success", "Upload terminé avec succès");
        } catch (EmptyFileException e) {
            redirectAttributes.addFlashAttribute("error", "Le fichier est vide !");
        } catch (InvalidExcelFormatException e) {
            redirectAttributes.addFlashAttribute("error", "Erreur de format dans le fichier Excel : " + e.getMessage());
        } catch (LibreOfficeConversionException e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la conversion PDF : " + e.getMessage());
        } catch ( IOException | InterruptedException e) {
            redirectAttributes.addFlashAttribute("error", "Erreur inattendue lors du traitement du fichier : " + e.getMessage());
        }
        // Rediriger vers la page admin/timetable
        return "redirect:/admin/timetable";
    }




}
