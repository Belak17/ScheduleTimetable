package com.belak.scheduletimetable.model;

import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;

import java.time.DayOfWeek;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "cours_TP")
public class CoursTP {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "tp_seq"
    )
    @SequenceGenerator(
            name = "tp_seq",
            sequenceName = "tp_sequence",
            allocationSize = 1
    )
    private Long id ;

    private  String intitule ;
    private String  dayOfWeek ;
    private  LocalTime debut ;
    private LocalTime fin ;
    private boolean inverseOfAnother = false;
    private Long dependsOnCoursId = null;
    @ManyToOne
    @JoinColumn(name = "grouptimetable_id")
    private GroupTimetable groupTimetable;

    @ManyToOne
    @JoinColumn(name = "salle_id")
    private Salle salle;

    private int frequence ;
    private  int rotationOffset ;

    @OneToMany(mappedBy = "coursTP", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seance> seances = new ArrayList<>();

    public void addSeance (Seance seance)
    {
        if (seances == null) {
            seances = new ArrayList<>();
        }
        seances.add(seance);
        seance.setCoursTP(this);
    }

    public boolean shouldOccurThisWeek(int weekNumber) {

        if (inverseOfAnother) {
            throw new IllegalStateException("Use service-level logic for dependent TP");
        }
        if (frequence <= 0) {
            throw new IllegalArgumentException("frequency must be > 0");
        }
        if (rotationOffset < 0 || rotationOffset >= frequence) {
            throw new IllegalArgumentException("offsetRotation invalide");
        }
        return weekNumber % frequence == rotationOffset;
    }



}
