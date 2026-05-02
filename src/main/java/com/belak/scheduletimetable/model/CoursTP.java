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
    private byte[] codeQr;
    private  String qrData ;
    private String  dayOfWeek ;
    private  LocalTime debut ;
    private LocalTime fin ;
    @ManyToOne
    @JoinColumn(name = "grouptimetable_id")
    private GroupTimetable groupTimetable;

    private int frequence ;
    private  int rotationOffset ;
    // pour donner la date de debut des cours
    private LocalDate startDate;

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

        if (frequence <= 0) {
            throw new IllegalArgumentException("frequency must be > 0");
        }

        if (rotationOffset < 0 || rotationOffset >= frequence) {
            throw new IllegalArgumentException("offsetRotation invalide");
        }
        // si date de debut donne , utiliser
        //   long weeksSinceStart = ChronoUnit.WEEKS.between(startDate, today);

        return weekNumber % frequence == rotationOffset;
    }



}
