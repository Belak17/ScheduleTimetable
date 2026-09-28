package com.belak.scheduletimetable.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cours_TP")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    private Long id;

    private String intitule;


    @Column(nullable = false)
    private String  dayOfWeek;

    private LocalTime debut;

    private LocalTime fin;

    @Column(nullable = false)
    private boolean inverseOfAnother = false;

    private Long dependsOnCoursId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "grouptimetable_id",
            nullable = false
    )
    private GroupTimetable groupTimetable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salle_id")
    private Salle salle;

    @Column(nullable = false)
    private int frequence;

    @Column(nullable = false)
    private int rotationOffset;

    @OneToMany(
            mappedBy = "coursTP",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Seance> seances = new ArrayList<>();

    public void addSeance(Seance seance) {
        if (seance == null) {
            throw new IllegalArgumentException("Seance cannot be null");
        }

        seances.add(seance);
        seance.setCoursTP(this);
    }

    public void removeSeance(Seance seance) {
        if (seance == null) {
            return;
        }

        seances.remove(seance);
        seance.setCoursTP(null);
    }

    public boolean shouldOccurThisWeek(int weekNumber) {

        if (inverseOfAnother) {
            throw new IllegalStateException(
                    "Dependent TP occurrence must be handled by the service"
            );
        }

        if (frequence <= 0) {
            throw new IllegalArgumentException(
                    "Frequency must be greater than 0"
            );
        }

        if (rotationOffset < 0 || rotationOffset >= frequence) {
            throw new IllegalArgumentException(
                    "Rotation offset must be between 0 and frequency - 1"
            );
        }

        return weekNumber % frequence == rotationOffset;
    }
}