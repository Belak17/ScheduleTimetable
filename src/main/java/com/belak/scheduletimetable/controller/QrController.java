package com.belak.scheduletimetable.controller;

import com.belak.scheduletimetable.model.Presence;
import com.belak.scheduletimetable.service.presence.PresenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/qr")
@RequiredArgsConstructor
public class QrController {
    private final PresenceService presenceService ;
    @PostMapping("/scan")
    public void scanQr(@RequestBody Map<String, String> body ,
                       Authentication authentication) {
        String code = body.get("code");
        presenceService.createPresence(authentication.getName(),code);
    }

}
