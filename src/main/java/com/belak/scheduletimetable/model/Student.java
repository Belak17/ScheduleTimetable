package com.belak.scheduletimetable.model;

import com.belak.scheduletimetable.dto.CreateStudentDto;
import com.belak.scheduletimetable.dto.StudentImportDto;
import com.belak.scheduletimetable.enumeration.Filiere;
import com.belak.scheduletimetable.enumeration.TypeDiplome;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "student")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Student  extends User {

    @Enumerated(EnumType.STRING)
    @Column(name = "student_field")
    private Filiere filiere;
    @Column(name = "student_year")

    private Integer niveau;

    private String codeDiplome;
    private String nomDiplome;

    @Enumerated(EnumType.STRING)
    private TypeDiplome typeDiplome;

    private String numeroInscription;

    private String groupeC;
    private String groupeTD;
    private String groupeM;
    private String groupeArchive;

    @Column(name = "student_group")
    private String group ;
    @ManyToMany(mappedBy = "students", fetch = FetchType.LAZY)
    private Set<GroupTimetable> timetables = new HashSet<>();

    @OneToMany(mappedBy = "student" , cascade = CascadeType.ALL , orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Presence> presences = new ArrayList<>();

    public void addPresence(Presence presence){
        if (presences == null) {
            presences = new ArrayList<>();
        }
        presence.setStudent(this);
        presences.add(presence);


    }
    public Student(CreateStudentDto dto) {
        this.setUserId(dto.getUserId());
        this.setCin(dto.getCin());
        this.setNom(dto.getNom());
        this.setPrenom(dto.getPrenom());
        this.setPrenomArabe(dto.getPrenomArabe());
        this.setNomArabe(dto.getNomArabe());
        this.setEmail(dto.getEmail());
        this.setTelephone(dto.getTelephone());
        this.setSexe(dto.getSexe());
        this.setDateNaissance(dto.getDateNaissance());
        this.setVilleNaissance(dto.getVilleNaissance());
        this.setVilleNaissanceArabe(dto.getVilleNaissanceArabe());
        this.setAdresse(dto.getAdresse());
        this.setCodePostal(dto.getCodePostal());
        this.setVille(dto.getVille());
        this.setNationalite(dto.getNationalite());
        this.setDepartment(dto.getDepartment());
        this.setCodeDepartement(dto.getCodeDepartement());
        this.setRole(dto.getRole());
        this.setFiliere(dto.getFiliere());
        this.setNiveau(dto.getNiveau());
        this.setCodeDiplome(dto.getCodeDiplome());
        this.setNomDiplome(dto.getNomDiplome());
        this.setTypeDiplome(dto.getTypeDiplome());
        this.setNumeroInscription(dto.getNumeroInscription());
        this.setGroup(dto.getGroup());
        this.setGroupeC(dto.getGroupeC());
        this.setGroupeTD(dto.getGroupeTD());
        this.setGroupeArchive(dto.getGroupeArchive());
        this.setGroupeM(dto.getGroupeM());
        this.setPassword(dto.getPassword());
    }

    public Student(StudentImportDto dto) {
        this.setUserId(dto.getUserId());
        this.setCin(dto.getCin());
        this.setNom(dto.getNom());
        this.setPrenom(dto.getPrenom());

        this.setEmail(dto.getEmail());
        this.setTelephone(dto.getTelephone());
        this.setSexe(dto.getSexe());
        this.setDateNaissance(dto.getDateNaissance());
        this.setVilleNaissance(dto.getVilleNaissance());

        this.setAdresse(dto.getAdresse());
        this.setCodePostal(dto.getCodePostal());
        this.setVille(dto.getVille());
        this.setNationalite(dto.getNationalite());
        this.setDepartment(dto.getDepartement());
        this.setCodeDepartement(dto.getDepartement().getCode());
        this.setRole(Role.STUDENT);
        this.setFiliere(dto.getFiliere());
        this.setNiveau(dto.getNiveau());
        this.setCodeDiplome(dto.getTypeDiplome().getCode());
        this.setNomDiplome(dto.getTypeDiplome().getLibelle());
        this.setTypeDiplome(dto.getTypeDiplome());
        this.setNumeroInscription(dto.getNumeroInscription());
        this.setGroup(dto.getGroup());
        this.setGroupeC(dto.getGroupeC());
        this.setGroupeTD(dto.getGroupeTD());
        this.setGroupeArchive(dto.getGroupeArchive());
        this.setGroupeM(dto.getGroupeM());

    }





}
