package com.belak.scheduletimetable.dto;

import com.belak.scheduletimetable.enumeration.*;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StudentImportDto {
    private  String userId ;
    private  String cin ;
    private  String nom ;
    private  String prenom ;
    private String email ;
    private  String  telephone;
    private  String sexe ;
    private LocalDate dateNaissance;
    private String villeNaissance;
    private String adresse ;
    private String codePostal;
    private String ville ;
    private Nationalite nationalite ;
    private Departement departement ;
    private Filiere filiere ;
    private Integer niveau;

    private TypeDiplome typeDiplome;

    private String numeroInscription;

    private String groupeC;
    private String groupeTD;
    private String groupeM;
    private String groupeArchive;

    private String group ;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
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

    public String getVilleNaissance() {
        return villeNaissance;
    }

    public void setVilleNaissance(String villeNaissance) {
        this.villeNaissance = villeNaissance;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
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

    public Filiere getFiliere() {
        return filiere;
    }

    public void setFiliere(Filiere filiere) {
        this.filiere = filiere;
    }

    public Integer getNiveau() {
        return niveau;
    }

    public void setNiveau(Integer niveau) {
        this.niveau = niveau;
    }

    public TypeDiplome getTypeDiplome() {
        return typeDiplome;
    }

    public void setTypeDiplome(TypeDiplome typeDiplome) {
        this.typeDiplome = typeDiplome;
    }

    public String getNumeroInscription() {
        return numeroInscription;
    }

    public void setNumeroInscription(String numeroInscription) {
        this.numeroInscription = numeroInscription;
    }

    public String getGroupeC() {
        return groupeC;
    }

    public void setGroupeC(String groupeC) {
        this.groupeC = groupeC;
    }

    public String getGroupeTD() {
        return groupeTD;
    }

    public void setGroupeTD(String groupeTD) {
        this.groupeTD = groupeTD;
    }

    public String getGroupeM() {
        return groupeM;
    }

    public void setGroupeM(String groupeM) {
        this.groupeM = groupeM;
    }

    public String getGroupeArchive() {
        return groupeArchive;
    }

    public void setGroupeArchive(String groupeArchive) {
        this.groupeArchive = groupeArchive;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
