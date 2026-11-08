package com.belak.scheduletimetable.dto.admin;

import com.belak.scheduletimetable.enumeration.Nationalite;
import lombok.AllArgsConstructor;
import lombok.Data;

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
