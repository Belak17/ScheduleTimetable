package com.belak.scheduletimetable.model;

import com.belak.scheduletimetable.dto.CreateProfessorDto;
import com.belak.scheduletimetable.dto.CreateStudentDto;
import com.belak.scheduletimetable.enumeration.Grade;
import com.belak.scheduletimetable.enumeration.Statuts;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "professor")
public class Professor extends User {
    private String specialite;
    private Grade grade ;
    private String codeGrade ;
    private String libelleGrade ;

    @Enumerated(EnumType.STRING)
    private Statuts schoolStatus ;
    private String codeStatus ;
    private String libelleStatus ;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "timetable_id") // Clé étrangère dans professor
    private ProfessorTimetable timetable;

    private String etablissement_origine ;
    private String rib ;
    private String application_tiers ;

    public Professor(CreateProfessorDto dto) {
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
        this.setSpecialite(dto.getSpecialite());
        this.setGrade(dto.getGrade());
        this.setCodeGrade(dto.getCodeGrade());
        this.setLibelleGrade(dto.getLibelleGrade());
        this.setSchoolStatus(dto.getSchoolStatus());
        this.setCodeStatus(dto.getCodeStatus());
        this.setLibelleStatus(dto.getLibelleStatus());
        this.setEtablissement_origine(dto.getEtablissement_origine());
        this.setRib(dto.getRib());
        this.setApplication_tiers(dto.getApplication_tiers());

    }


}
