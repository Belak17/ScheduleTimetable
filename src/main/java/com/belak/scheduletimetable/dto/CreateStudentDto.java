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
@SuperBuilder
@NoArgsConstructor
public class CreateStudentDto extends  CreateUserDto{

    private Filiere filiere;

    private String niveau;

    private String codeDiplome;

    private String nomDiplome;

    private TypeDiplome typeDiplome;

    private String numeroInscription;

    private String group;

    public Filiere getFiliere() {
        return filiere;
    }

    public void setFiliere(Filiere filiere) {
        this.filiere = filiere;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public String getCodeDiplome() {
        return codeDiplome;
    }

    public void setCodeDiplome(String codeDiplome) {
        this.codeDiplome = codeDiplome;
    }

    public String getNomDiplome() {
        return nomDiplome;
    }

    public void setNomDiplome(String nomDiplome) {
        this.nomDiplome = nomDiplome;
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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
