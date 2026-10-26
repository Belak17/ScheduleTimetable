package com.belak.scheduletimetable.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

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
        redirectAttributes.addFlashAttribute(
                "registrationTime",
                ex.getTime().format(DateTimeFormatter.ofPattern("HH:mm"))
        );
        redirectAttributes.addFlashAttribute("dayOfWeek", ex.getDayOfWeek());
        redirectAttributes.addFlashAttribute("date", ex.getDate().format(DateTimeFormatter.ISO_DATE));
        redirectAttributes.addFlashAttribute("nom",ex.getNom());
        redirectAttributes.addFlashAttribute("prenom",ex.getPrenom());
        return "redirect:/student/alreadyRegistered";
    }

    @ExceptionHandler(CourseAndCodeNotFoundException.class)
    public String handleCode(CourseAndCodeNotFoundException ex, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        redirectAttributes.addFlashAttribute("code", ex.getCode());
        redirectAttributes.addFlashAttribute("userId", ex.getUserId());
        redirectAttributes.addFlashAttribute("nom",ex.getNom());
        redirectAttributes.addFlashAttribute("prenom",ex.getPrenom());
        return "redirect:/student/codeNotFound";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(
            IllegalArgumentException ex,
            Model model) {

        logger.warn("Argument invalide : {}", ex.getMessage(), ex);

        model.addAttribute(
                "errorMessage",
                ex.getMessage()
        );

        return "student/exceptionError";
    }


    @ExceptionHandler(AccountNotActivatedException.class)
    public String handleAccountNotActivated(
            AccountNotActivatedException ex,
            Model model) {

        model.addAttribute("errorMessage", ex.getMessage());

        return "login/login";
    }

    @ExceptionHandler(ResetNotPossibleAccountNotActivatedException.class)
    public String handleResetNotPossibleAccountNotActivated(
            AccountNotActivatedException ex,
            Model model) {

        model.addAttribute("errorMessage", ex.getMessage());

        return "login/forgot";
    }


    @ExceptionHandler(StudentNotFoundException.class)
    public String handleStudentNotFoundException(
            StudentNotFoundException ex,
            Model model) {

        model.addAttribute("errorMessage", ex.getMessage());

        model.addAttribute("userId", ex.getUserId());

        model.addAttribute("code",ex.getCode());

        return "student/studentNotFound";
    }
    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDenied(
            AccessDeniedException ex,
            Model model) {

        logger.warn("Accès refusé : {}", ex.getMessage());

        model.addAttribute(
                "errorMessage",
                "Vous n'avez pas les autorisations nécessaires pour accéder à cette ressource."
        );

        return "student/exceptionError";
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(
            ResourceNotFoundException ex,
            Model model) {

        logger.warn("Ressource introuvable : {}", ex.getMessage());

        model.addAttribute("errorMessage", ex.getMessage());

        return "student/exceptionError";
    }

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(
            RuntimeException ex,
            Model model) {

        logger.error("Erreur d'exécution inattendue", ex);

        model.addAttribute(
                "errorMessage",
                "Une erreur inattendue s'est produite. Veuillez réessayer ultérieurement."
        );

        return "student/exceptionError";
    }


    @ExceptionHandler(Exception.class)
    public String handleException(
            Exception ex,
            Model model) {

        logger.error("Erreur inattendue", ex);

        model.addAttribute(
                "errorMessage",
                "Une erreur interne est survenue. Veuillez réessayer ultérieurement."
        );

        return "student/exceptionError";
    }


}
