package com.belak.scheduletimetable.service.admin;

import com.belak.scheduletimetable.dto.AdminProfileDto;
import com.belak.scheduletimetable.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;

    public AdminProfileDto findByUserId(String userId) {
        return userRepository.findByUserId(userId)
                .map(user -> new AdminProfileDto(
                        user.getCin(),
                        user.getNom(),
                        user.getPrenom(),
                        user.getEmail(),
                        user.getTelephone(),
                        user.getSexe(),
                        user.getVilleNaissance(),
                        user.getAdresse(),
                        user.getCodePostal(),
                        user.getVille(),
                        user.getNationalite()
                ))
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
    }
}
