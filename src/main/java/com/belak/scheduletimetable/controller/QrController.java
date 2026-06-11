package com.belak.scheduletimetable.controller;

import com.belak.scheduletimetable.dto.PresenceValidationDto;
import com.belak.scheduletimetable.model.Presence;
import com.belak.scheduletimetable.service.presence.PresenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Controller
@RequestMapping("/api/qr")
@RequiredArgsConstructor
public class QrController {

    private final PresenceService presenceService;

    @GetMapping("/scan")
    public String scanQr(@RequestParam String code,
                         Authentication authentication) {

        PresenceValidationDto dto = presenceService.createPresence(
                authentication.getName(),
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
