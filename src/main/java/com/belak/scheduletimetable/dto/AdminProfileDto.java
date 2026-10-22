package com.belak.scheduletimetable.dto;

import com.belak.scheduletimetable.enumeration.Nationalite;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class AdminProfileDto {
    private String  userId;

    private String nom;

    private String prenom;

    private String email;

    private String telephone;

    private String sexe;

    private Nationalite nationalite;


}
