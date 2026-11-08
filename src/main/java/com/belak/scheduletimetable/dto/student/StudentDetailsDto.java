package com.belak.scheduletimetable.dto.student;

import com.belak.scheduletimetable.enumeration.Departement;
import com.belak.scheduletimetable.enumeration.Filiere;
import com.belak.scheduletimetable.enumeration.Nationalite;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentDetailsDto {

    private  Long id;
    private  String userId ;


    private String nom; //nom
    private String prenom;//prenom


    private  String password ;
    private String email ;
    private String telephone;
    private String sexe;

    private Nationalite nationalite;

    private Departement department ;

    private Filiere filiere;
    private String niveau;
    private String group ;
    private boolean enabled;
}
