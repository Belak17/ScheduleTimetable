package com.belak.scheduletimetable.service.admin;

import com.belak.scheduletimetable.dto.AdminProfileDto;
import com.belak.scheduletimetable.model.Student;
import com.belak.scheduletimetable.model.User;
import com.belak.scheduletimetable.repository.AdminRepository;
import com.belak.scheduletimetable.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService implements AdminInterfaceService {
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    public AdminProfileDto findByUserId(String userId) {
        return userRepository.findByUserId(userId)
                .map(user -> new AdminProfileDto(
                        user.getUserId(),
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

    public void updateEmail(String nouvEmail, String confEmail, String userId) {
        if (!nouvEmail.equals(confEmail)) {
            throw new IllegalArgumentException("Les emails ne correspondent pas");
        }

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Admin introuvable"));

        user.setEmail(nouvEmail);
        userRepository.save(user);
    }

    public void updateInfo(String userId, String address, String telephone, String code) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Admin introuvable"));
        user.setUserId(userId);
        user.setAdresse(address);
        user.setTelephone(telephone);
        user.setCodePostal(code);
        userRepository.save(user);
    }

    public void updatePassword(String oldPassword,
                               String newPassword,
                               String confPassword,
                               String userId) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Admin introuvable"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Ancien mot de passe incorrect");
        }

        if (!newPassword.equals(confPassword)) {
            throw new IllegalArgumentException("Les mots de passe ne correspondent pas");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
