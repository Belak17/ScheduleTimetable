package com.belak.scheduletimetable.controller;

import com.belak.scheduletimetable.dto.PresenceValidationDto;
import com.belak.scheduletimetable.model.Presence;
import com.belak.scheduletimetable.service.presence.PresenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Controller
@RequestMapping("/api/qr")
@RequiredArgsConstructor
@Slf4j
public class QrController {
    private final PresenceService presenceService;
    @GetMapping("/scan")
    public String scanQr(@RequestParam String code,
                         Authentication authentication) {



        if (authentication!=null&& authentication.isAuthenticated()) {
            log.info("Utlisateur Authentifié Pour le Scannage");
            PresenceValidationDto dto = presenceService.createPresence(
                    authentication.getName(),
                    code
            );
            String url = UriComponentsBuilder
                    .fromPath("/student/validation")
                    .queryParam("intitule", dto.getIntitule())
                    .queryParam("group", dto.getGroup())
                    .queryParam("code", dto.getCode())
                    .queryParam("date", dto.getDate().format(DateTimeFormatter.ISO_DATE))
                    .queryParam("time", dto.getTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                    .queryParam("day", dto.getDay())
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();

            return "redirect:" + url;
        }

        log.info("Utilisateur Non Authentifie.Debut Demande Identifiant Pour Verification");

        return "redirect:/api/qr/authentify"+
                "?code=" + code;
    }
    @PostMapping("/change/salle")
    public String changeSalle(Authentication authentication , @RequestParam String code , @RequestParam(required = false) String userId)
    {
        log.info("Changement de Salle Pour le Cours de TP");
        String username ;
        if (authentication!=null) {
            username = authentication.getName();
            log.info("Utilisateur Authentifie");
        }
        else
        {
            username = userId;
            log.info("Utlisateur Non Authentifie");
        }
        PresenceValidationDto dto = presenceService.createPresenceWithoutCode(
                username, code
        );

        String url = UriComponentsBuilder
                .fromPath("/student/validation")
                .queryParam("intitule", dto.getIntitule())
                .queryParam("group", dto.getGroup())
                .queryParam("code", dto.getCode())
                .queryParam("date", dto.getDate().format(DateTimeFormatter.ISO_DATE))
                .queryParam("time", dto.getTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                .queryParam("day", dto.getDay())
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();
        log.info("Confirmation du Changement de Salle et Seance de Cours Disponible");
        return "redirect:" + url;
    }

    @GetMapping("/authentify")
    public String authentifyForScanningCodeQr(@RequestParam String code , Model model)
    {
        log.info("Appel de la page de Demande Identifiant");
        model.addAttribute("code", code);
        return  "student/authenticationPage";
    }


    @PostMapping("/scan/authenticated")
    public String registerPresenceNowAuthenticated(@RequestParam String code, @RequestParam String userId) {



            log.info("Confirmation Presence avec Utlisateur Authenfie via authenticationPage");


            PresenceValidationDto dto = presenceService.createPresence(
                    userId,
                    code
            );
            return "redirect:/student/validation"
                    + "?intitule=" + dto.getIntitule()
                    + "&group=" + dto.getGroup()
                    + "&code=" + dto.getCode()
                    + "&date=" + dto.getDate().format(DateTimeFormatter.ISO_DATE)
                    + "&time=" + dto.getTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                    + "&day=" + dto.getDay();
        }



}
