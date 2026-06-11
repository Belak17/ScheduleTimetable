package com.belak.scheduletimetable.data;

import com.belak.scheduletimetable.enumeration.*;
import com.belak.scheduletimetable.model.Professor;
import com.belak.scheduletimetable.model.Salle;
import com.belak.scheduletimetable.model.Student;
import com.belak.scheduletimetable.model.User;
import com.belak.scheduletimetable.repository.ProfessorRepository;
import com.belak.scheduletimetable.repository.SalleRepository;
import com.belak.scheduletimetable.repository.StudentRepository;
import com.belak.scheduletimetable.repository.UserRepository;
import com.belak.scheduletimetable.service.courstp.SalleService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner
{
    private  final UserRepository userRepository ;
    private  final ProfessorRepository professorRepository ;
    private  final PasswordEncoder passwordEncoder ;
    private final StudentRepository studentRepository ;
    private  final SalleRepository salleRepository ;
    private  final SalleService salleService ;
    @Override
    public void run(String... args) throws Exception {
        // Vérifie si un utilisateur avec userId "admin" existe
        if (userRepository.findByUserId("BJ240005").isEmpty()) {
            // Crée un nouvel admin
            User admin = new User();
            admin.setUserId("BJ240005"); // identifiant admin
            admin.setPassword(passwordEncoder.encode("BJ240005"));// mot de passe hashé
            admin.setCin("24PP37392");
            admin.setRole(User.Role.valueOf("ADMIN")); // rôle
            admin.setEmail("akabeb.com@gmail.com");
            admin.setPrenom("Yelongnise Kaleb Renaud Gerald");
            admin.setNom("AKAKPO");
            // Sauvegarde dans la base
            userRepository.save(admin);
            System.out.println("Admin créé avec succès !");
        }

        if (userRepository.findByUserId("BJ340005").isEmpty())
        {
            Professor professor = new Professor();
            professor.setUserId("BJ340005");
            professor.setPassword(passwordEncoder.encode("BJ340005"));
            professor.setCin("2403994");
            professor.setRole(User.Role.valueOf("PROFESSOR"));
            professor.setEmail("akakpokaleb09@gmail.com");
            professor.setNom("Afef");
            professor.setPrenom("Troudi");
            professor.setGrade(Grade.MAITRE_ASSISTANT);
            professor.setSchoolStatus(Statuts.PERMANENT);
            professor.setDepartment(Departement.ST);
            professor.setSpecialite("Biologie");
            professorRepository.save(professor);

        }
        if (userRepository.findByUserId("BJ440005").isEmpty())
        {
            Student student = new Student();
            student.setUserId("BJ440005");
            student.setPassword(passwordEncoder.encode("BJ440005"));
            student.setRole(User.Role.valueOf("STUDENT"));
            student.setCin("2394595");
            student.setNom("AKAKP");
            student.setPrenom("Renaud");
            student.setEmail("akakpo@gmail.com");
            student.setNiveau(2);
            student.setFiliere(Filiere.LSI);
            student.setGroup("TD1 TP1");
            student.setTypeDiplome(TypeDiplome.LICENCE);
            student.setDepartment(Departement.INF);
            studentRepository.save(student);

        }

    }

    @PostConstruct
    public void initSalles() {


        if (salleRepository.count() > 0) {
            return; // déjà initialisé → on sort
        }

        List<Salle> salles = new ArrayList<>();

        for (int i = 1; i <= 40; i++) {
            salles.add(createSalle("S" + i));
        }
        salles.add(createSalle("GI1"));
        generateRange("A", salles);
        generateRange("B", salles);
        generateRange("C", salles);
        generateRange("D", salles);
        salleRepository.saveAll(salles);
    }

    private void generateRange(String prefix, List<Salle> salles) {
        for (int i = 0; i <= 1; i++) {
            for (int j = 1; j <= 30; j++) {
                salles.add(createSalle(prefix + i + "." + j));
            }
        }
    }

    private Salle createSalle(String code) {
        Salle s = new Salle();
        s.setCode(code);
        s.setCodeQr(salleService.generateQrCode(s));
        return s;
    }
}
