package com.belak.scheduletimetable.data;

import com.belak.scheduletimetable.enumeration.*;
import com.belak.scheduletimetable.model.Professor;
import com.belak.scheduletimetable.model.Student;
import com.belak.scheduletimetable.model.User;
import com.belak.scheduletimetable.repository.ProfessorRepository;
import com.belak.scheduletimetable.repository.StudentRepository;
import com.belak.scheduletimetable.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner
{
    private  final UserRepository userRepository ;
    private  final ProfessorRepository professorRepository ;
    private  final PasswordEncoder passwordEncoder ;
    private final StudentRepository studentRepository ;
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
            student.setGroup("A");
            student.setTypeDiplome(TypeDiplome.LICENCE);
            student.setDepartment(Departement.INF);
            studentRepository.save(student);

        }

    }
}
