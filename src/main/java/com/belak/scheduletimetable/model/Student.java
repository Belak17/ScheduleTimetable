package com.belak.scheduletimetable.model;

import com.belak.scheduletimetable.dto.CreateStudentDto;
import com.belak.scheduletimetable.enumeration.Filiere;
import com.belak.scheduletimetable.enumeration.TypeDiplome;
import jakarta.persistence.*;
import lombok.*;

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
    @ManyToOne
    @JoinColumn(name = "grouptimetable_id")
    private GroupTimetable groupTimetable;


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





}
