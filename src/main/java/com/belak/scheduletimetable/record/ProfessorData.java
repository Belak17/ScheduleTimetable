package com.belak.scheduletimetable.record;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfessorData {
    String prenom;
    String nom;
    String grade ;
    String statut ;
    String speciality;
}
