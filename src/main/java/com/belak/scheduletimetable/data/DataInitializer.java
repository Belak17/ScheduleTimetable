package com.belak.scheduletimetable.data;

import com.belak.scheduletimetable.enumeration.*;
import com.belak.scheduletimetable.model.*;
import com.belak.scheduletimetable.repository.*;
import com.belak.scheduletimetable.service.courstp.SalleService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Slf4j
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
    private  final GroupTimetableRepository groupTimetableRepository ;
    private  final ConfirmationTokenRepository confirmationTokenRepository ;
    @Override
    @Transactional

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
            student.setEmail("akanaud17@gmail.com");
            student.setNiveau(2);
            student.setFiliere(Filiere.LSI);
            student.setGroup("TD3 TP1");
            student.setTypeDiplome(TypeDiplome.LICENCE);
            student.setDepartment(Departement.INF);
            studentRepository.save(student);

        }

        if (userRepository.findByUserId("TD550005").isEmpty())
        {
            Student student = new Student();
            student.setUserId("TD550005");
            student.setPassword(passwordEncoder.encode("TD550005"));
            student.setRole(User.Role.valueOf("STUDENT"));
            student.setCin("2396795");
            student.setNom("AKAK");
            student.setPrenom("Ren Yelongnise");
            student.setEmail("thefool1709@gmail.com");
            student.setNiveau(2);
            student.setFiliere(Filiere.LIRS);
            student.setGroup("TD1 TP1");
            student.setTypeDiplome(TypeDiplome.LICENCE);
            student.setDepartment(Departement.INF);
            studentRepository.save(student);

        }
        List<GroupTimetable> timetables =
                groupTimetableRepository.findByFiliereAndNiveau(
                        Filiere.LIRS,
                        2
                );

        for (GroupTimetable timetable : timetables) {
            log.info("Groupe : {}", timetable.getGroup());
        }

//        Optional<Student> studentOpt = studentRepository.findByUserId("KL550005");
//
//        if (studentOpt.isPresent()) {
//
//            Student student = studentOpt.get();
//
//            // Retirer l'étudiant de ses emplois du temps
//            for (GroupTimetable timetable : new HashSet<>(student.getTimetables())) {
//                timetable.getStudents().remove(student);
//            }
//
//            student.getTimetables().clear();
//
//            // Supprimer les tokens de confirmation liés à cet utilisateur
//            confirmationTokenRepository.deleteByAppUser(student);
//
//            // Supprimer l'étudiant
//            studentRepository.delete(student);
//
//            log.info("Étudiant {} supprimé", student.getUserId());
//
//        } else {
//            log.warn("Étudiant KL550005 introuvable");
//        }

        if (userRepository.findByUserId("KL550005").isEmpty())
        {
            Student student = new Student();
            student.setUserId("KL550005");
            student.setPassword(passwordEncoder.encode("KL550005"));
            student.setRole(User.Role.valueOf("STUDENT"));
            student.setCin("4496795");
            student.setNom("AKK");
            student.setPrenom("Ren Yelogse");
            student.setEmail("alimatouakakpo2018@gmail.com");
            student.setNiveau(1);
            student.setFiliere(Filiere.LSI);
            student.setGroup("A TD2 TP1");
            student.setTypeDiplome(TypeDiplome.LICENCE);
            student.setDepartment(Departement.INF);

            studentRepository.save(student);

            Optional<GroupTimetable> timetable = Optional.ofNullable(
                    groupTimetableRepository.findByDepartementAndFiliereAndGroupAndNiveau(
                            Departement.INF,
                            Filiere.LSI,
                            "A TD2 TP1",
                            1
                    )
            );

            if (timetable.isPresent()) {
                timetable.get().addStudent(student);
                log.info("Étudiant ajouté à l'emploi du temps");
            } else {
                log.warn("Emploi du temps introuvable");
            }
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
