package com.belak.scheduletimetable.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;

@Entity
@Table(name = "presence",    uniqueConstraints = @UniqueConstraint(
        columnNames = {"student_id", "seance_id"}))
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Presence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean present;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "seance_id")
    private Seance seance;

    private LocalTime localTime;

    public boolean getPresent() {
        return present ;
    }
}
