package com.belak.scheduletimetable.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ElementNotFoundException.class)
    public String handle(ElementNotFoundException ex, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());

        return "redirect:/student/error";
    }


    @ExceptionHandler(PresenceAlreadyExistsException.class)
    public String handlePresence(PresenceAlreadyExistsException ex, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        redirectAttributes.addFlashAttribute("intitule", ex.getIntitule());
        redirectAttributes.addFlashAttribute("salle", ex.getSalle());
        redirectAttributes.addFlashAttribute("date", ex.getDate());
        redirectAttributes.addFlashAttribute("dayOfWeek", ex.getDayOfWeek());

        return "redirect:/student/alreadyRegistered";
    }

    @ExceptionHandler(CourseAndCodeNotFoundException.class)
    public String handleCode(CourseAndCodeNotFoundException ex, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        redirectAttributes.addFlashAttribute("code", ex.getCode());
        return "redirect:/student/codeNotFound";
    }


}
