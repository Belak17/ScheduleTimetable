package com.belak.scheduletimetable.dto;

import com.belak.scheduletimetable.enumeration.Departement;
import com.belak.scheduletimetable.enumeration.Filiere;
import com.belak.scheduletimetable.enumeration.Nationalite;
import com.belak.scheduletimetable.enumeration.TypeDiplome;
import com.belak.scheduletimetable.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class CreateStudentDto extends  CreateUserDto{

    private Filiere filiere;
    private Integer niveau;
    private String codeDiplome;
    private String nomDiplome;
    private TypeDiplome typeDiplome;
    private String numeroInscription;

    //private String groupeC;
    //private String groupeTD;
    //private String groupeM;
    //private String groupeArchive;

    private String group ;



}
