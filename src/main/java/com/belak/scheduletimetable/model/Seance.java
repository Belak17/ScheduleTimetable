package com.belak.scheduletimetable.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Seance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "cours_tp_id")
    private CoursTP coursTP;

    @OneToMany(mappedBy = "seance", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Presence> presences= new ArrayList<>();

    public void addPresence (Presence presence)
    {
        if (presences == null) {
            presences = new ArrayList<>();
        }
        presences.add(presence);
        presence.setSeance(this);
    }
}
