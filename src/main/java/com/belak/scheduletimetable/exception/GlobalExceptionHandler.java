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


}
