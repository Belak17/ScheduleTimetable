package com.belak.scheduletimetable.dto;


import com.belak.scheduletimetable.enumeration.Departement;
import com.belak.scheduletimetable.enumeration.Grade;
import com.belak.scheduletimetable.enumeration.Nationalite;
import com.belak.scheduletimetable.enumeration.Statuts;
import com.belak.scheduletimetable.model.ProfessorTimetable;
import com.belak.scheduletimetable.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import com.belak.scheduletimetable.dto.CreateUserDto;
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
public class CreateProfessorDto extends CreateUserDto {
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

    public String getIdentifiantUnique() {
        return identifiantUnique;
    }

    public void setIdentifiantUnique(String identifiantUnique) {
        this.identifiantUnique = identifiantUnique;
    }

    public String getNomDepartement() {
        return nomDepartement;
    }

    public void setNomDepartement(String nomDepartement) {
        this.nomDepartement = nomDepartement;
    }

    private String specialite;
    private Grade grade ;
    private String codeGrade ;
    private String libelleGrade ;
    private Statuts schoolStatus ;
    private String codeStatus ;
    private String libelleStatus ;
    private String etablissement_origine ;
    private String rib ;
    private String application_tiers ;
    private  String identifiantUnique ;
    private String nomDepartement ;

}
