package com.belak.scheduletimetable.dto.coursTP;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class SeanceDto {

    private LocalDate date;
    private String intitule;
    private String salle;
    private LocalTime debut;
    private LocalTime fin;
    private boolean absencesProcessed;
}