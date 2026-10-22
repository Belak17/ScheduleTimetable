package com.belak.scheduletimetable.dto;

import com.belak.scheduletimetable.enumeration.Departement;
import com.belak.scheduletimetable.enumeration.Grade;
import com.belak.scheduletimetable.enumeration.Nationalite;
import com.belak.scheduletimetable.enumeration.Statuts;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProfessorImportDto {
    private  String userId ;

    private  String nom ;
    private  String prenom ;

    private String email ;
    private  String  telephone;
    private  String sexe ;
    private LocalDate dateNaissance;


    private Nationalite nationalite ;
    private Departement departement ;
    private String specialite ;
    private Statuts schoolStatus ;
    private Grade grade ;
    private  String rib ;
    private String etablissement_origine ;
    private  String application_tiers ;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public Nationalite getNationalite() {
        return nationalite;
    }

    public void setNationalite(Nationalite nationalite) {
        this.nationalite = nationalite;
    }

    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public Statuts getSchoolStatus() {
        return schoolStatus;
    }

    public void setSchoolStatus(Statuts schoolStatus) {
        this.schoolStatus = schoolStatus;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public String getRib() {
        return rib;
    }

    public void setRib(String rib) {
        this.rib = rib;
    }

    public String getEtablissement_origine() {
        return etablissement_origine;
    }

    public void setEtablissement_origine(String etablissement_origine) {
        this.etablissement_origine = etablissement_origine;
    }

    public String getApplication_tiers() {
        return application_tiers;
    }

    public void setApplication_tiers(String application_tiers) {
        this.application_tiers = application_tiers;
    }
}
