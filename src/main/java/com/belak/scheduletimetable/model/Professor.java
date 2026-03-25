package com.belak.scheduletimetable.model;

import com.belak.scheduletimetable.enumeration.Grade;
import com.belak.scheduletimetable.enumeration.Statuts;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "professor")
public class Professor extends User {
    private String specialite;
    private Grade grade ;
    private String codeGrade ;
    private String libelleGrade ;

    @Enumerated(EnumType.STRING)
    private Statuts schoolStatus ;
    private String codeStatus ;
    private String libelleStatus ;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "timetable_id") // Clé étrangère dans professor
    private ProfessorTimetable timetable;

    private String etablissement_origine ;
    private String rib ;
    private String application_tiers ;


}
