package com.belak.scheduletimetable.model;

import com.belak.scheduletimetable.dto.CreateProfessorDto;
import com.belak.scheduletimetable.dto.ProfessorImportDto;
import com.belak.scheduletimetable.enumeration.Grade;
import com.belak.scheduletimetable.enumeration.Statuts;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "professor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Professor extends User {


    private String grade;


    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "timetable_id")
    private ProfessorTimetable timetable;



    private String nomDepartement;


    public Professor(ProfessorImportDto dto) {
        this.setUserId(dto.getUserId());

        this.setNom(dto.getNom());
        this.setPrenom(dto.getPrenom());
        this.setEmail(dto.getEmail());
        this.setTelephone(dto.getTelephone());
        this.setSexe(dto.getSexe());
        this.setDateNaissance(dto.getDateNaissance());

        this.setNationalite(dto.getNationalite());
        this.setDepartment(dto.getDepartement());
        this.setRole(Role.PROFESSOR);

        this.setGrade(dto.getGrade());
        this.setNomDepartement(dto.getDepartement().getLibelle());
    }

    public Professor(CreateProfessorDto dto) {
        this.setUserId(dto.getUserId());

        this.setNom(dto.getNom());
        this.setPrenom(dto.getPrenom());

        this.setEmail(dto.getEmail());
        this.setTelephone(dto.getTelephone());
        this.setSexe(dto.getSexe());
        this.setDateNaissance(dto.getDateNaissance());

        this.setNationalite(dto.getNationalite());

        this.setRole(dto.getRole());
        this.setGrade(dto.getGrade());

        this.setPassword(dto.getPassword());
        this.setNomDepartement(dto.getNomDepartement());
    }

}