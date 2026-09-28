package com.belak.scheduletimetable.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "seance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Seance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private boolean absencesProcessed = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cours_tp_id", nullable = false)
    private CoursTP coursTP;

    @OneToMany(
            mappedBy = "seance",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Presence> presences = new ArrayList<>();

    public void addPresence(Presence presence) {
        if (presence == null) {
            throw new IllegalArgumentException("Presence cannot be null");
        }

        presences.add(presence);
        presence.setSeance(this);
    }

    public void removePresence(Presence presence) {
        if (presence == null) {
            return;
        }

        presences.remove(presence);
        presence.setSeance(null);
    }
}