package com.belak.scheduletimetable.dto.coursTP;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class PlanningSeanceDTO {




    private Long coursId;
    private String intitule;
    private final String groupe ;
    private String salle;

    private LocalDate date;
    private LocalTime heureDebut;
    private LocalTime heureFin;

    private boolean generated;
    private boolean absencesProcessed;
}