package com.belak.scheduletimetable.model;

import com.belak.scheduletimetable.dto.CreateProfessorDto;
import com.belak.scheduletimetable.dto.CreateStudentDto;
import com.belak.scheduletimetable.dto.ProfessorImportDto;
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

    private String nomDepartement ;

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
        this.setPassword(dto.getPassword());

        this.setNomDepartement(dto.getNomDepartement());

    }

    public Professor(ProfessorImportDto dto) {
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
        this.setRole(Role.PROFESSOR);
        this.setSpecialite(dto.getSpecialite());
        this.setGrade(dto.getGrade());
        this.setCodeGrade(dto.getGrade().getCode()) ;
        this.setLibelleGrade(dto.getGrade().getLibelle());
        this.setSchoolStatus(dto.getSchoolStatus());
        this.setCodeStatus(dto.getSchoolStatus().getCode());
        this.setLibelleStatus(dto.getSchoolStatus().getLibelle());
        this.setEtablissement_origine(dto.getEtablissement_origine());
        this.setRib(dto.getRib());
        this.setApplication_tiers(dto.getApplication_tiers());


        this.setNomDepartement(dto.getDepartement().getLibelle());

    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public String getCodeGrade() {
        return codeGrade;
    }

    public void setCodeGrade(String codeGrade) {
        this.codeGrade = codeGrade;
    }

    public String getLibelleGrade() {
        return libelleGrade;
    }

    public void setLibelleGrade(String libelleGrade) {
        this.libelleGrade = libelleGrade;
    }

    public Statuts getSchoolStatus() {
        return schoolStatus;
    }

    public void setSchoolStatus(Statuts schoolStatus) {
        this.schoolStatus = schoolStatus;
    }

    public String getCodeStatus() {
        return codeStatus;
    }

    public void setCodeStatus(String codeStatus) {
        this.codeStatus = codeStatus;
    }

    public String getLibelleStatus() {
        return libelleStatus;
    }

    public void setLibelleStatus(String libelleStatus) {
        this.libelleStatus = libelleStatus;
    }

    public ProfessorTimetable getTimetable() {
        return timetable;
    }

    public void setTimetable(ProfessorTimetable timetable) {
        this.timetable = timetable;
    }

    public String getEtablissement_origine() {
        return etablissement_origine;
    }

    public void setEtablissement_origine(String etablissement_origine) {
        this.etablissement_origine = etablissement_origine;
    }

    public String getRib() {
        return rib;
    }

    public void setRib(String rib) {
        this.rib = rib;
    }

    public String getApplication_tiers() {
        return application_tiers;
    }

    public void setApplication_tiers(String application_tiers) {
        this.application_tiers = application_tiers;
    }

    public String getNomDepartement() {
        return nomDepartement;
    }

    public void setNomDepartement(String nomDepartement) {
        this.nomDepartement = nomDepartement;
    }
}
