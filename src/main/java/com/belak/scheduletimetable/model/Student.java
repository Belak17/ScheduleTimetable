package com.belak.scheduletimetable.model;

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

    //private String groupeC;
    //private String groupeTD;
    //private String groupeM;
    //private String groupeArchive;

    @Column(name = "student_group")
    private String group ;
    @ManyToOne
    @JoinColumn(name = "grouptimetable_id")
    private GroupTimetable groupTimetable;





}
