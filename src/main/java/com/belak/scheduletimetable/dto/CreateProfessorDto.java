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

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
public class CreateProfessorDto extends CreateUserDto {
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
