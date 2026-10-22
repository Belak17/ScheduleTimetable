package com.belak.scheduletimetable.model;

import com.belak.scheduletimetable.dto.CreateStudentDto;
import com.belak.scheduletimetable.dto.StudentImportDto;
import com.belak.scheduletimetable.enumeration.Filiere;
import com.belak.scheduletimetable.enumeration.TypeDiplome;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "student")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student extends User {

    @Enumerated(EnumType.STRING)
    @Column(name = "student_field")
    private Filiere filiere;

    @Column(name = "student_year")
    private String niveau;

    private String codeDiplome;

    private String nomDiplome;

    @Enumerated(EnumType.STRING)
    private TypeDiplome typeDiplome;

    private String numeroInscription;


    @Column(name = "student_group")
    private String group;

    @ManyToMany(
            mappedBy = "students",
            fetch = FetchType.LAZY
    )
    private Set<GroupTimetable> timetables = new HashSet<>();

    @OneToMany(
            mappedBy = "student",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Presence> presences = new ArrayList<>();


    // =========================
    // Presence management
    // =========================

    public void addPresence(Presence presence) {
        if (presence == null) {
            throw new IllegalArgumentException("Presence cannot be null");
        }

        presence.setStudent(this);
        presences.add(presence);
    }

    public void removePresence(Presence presence) {
        if (presence == null) {
            return;
        }

        presences.remove(presence);
        presence.setStudent(null);
    }

    public Student(CreateStudentDto dto) {

        this.setId(dto.getId());
        this.setPassword(dto.getPassword());
        this.setNom(dto.getNom());
        this.setPrenom(dto.getPrenom());

        this.setUserId(dto.getUserId());
        this.setNationalite(dto.getNationalite());
        this.setSexe(dto.getSexe());
        this.setEmail(dto.getEmail());
        this.setDateNaissance(dto.getDateNaissance());
        this.setIsEmailVerified(dto.getIsEmailVerified());
        this.setTelephone(dto.getTelephone());

        this.filiere = dto.getFiliere();
        this.niveau = dto.getNiveau();
        this.codeDiplome = dto.getCodeDiplome();
        this.nomDiplome = dto.getNomDiplome();
        this.typeDiplome = dto.getTypeDiplome();
        this.numeroInscription = dto.getNumeroInscription();
        this.group = dto.getGroup();
        this.setRole(Role.STUDENT);
    }
    public Student(StudentImportDto dto) {
        this.setId(dto.getId());
        this.setPassword(dto.getPassword());
        this.setNom(dto.getNom());
        this.setPrenom(dto.getPrenom());
        this.setUserId(dto.getUserId());
        this.setNationalite(dto.getNationalite());
        this.setSexe(dto.getSexe());
        this.setEmail(dto.getEmail());
        this.setDateNaissance(dto.getDateNaissance());
        this.setIsEmailVerified(dto.getIsEmailVerified());
        this.setTelephone(dto.getTelephone());

        this.setFiliere(dto.getFiliere());
        this.setNiveau(dto.getNiveau());
        this.setCodeDiplome(dto.getCodeDiplome());
        this.setNomDiplome(dto.getNomDiplome());
        this.setTypeDiplome(dto.getTypeDiplome());
        this.setNumeroInscription(dto.getNumeroInscription());
        this.setGroup(dto.getGroup());
        this.setRole(Role.STUDENT);
    }
}