package com.belak.scheduletimetable.exception;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

public class PresenceAlreadyExistsException extends RuntimeException {
    @Getter
    private final String intitule;
    @Getter
    private final LocalDate date;

    @Getter
    private final LocalTime time;
    @Getter
    private final String dayOfWeek;

    @Getter
    private final String salle;


    public PresenceAlreadyExistsException(String présenceDéjàEnregistrée, String intitule, LocalDate date, String dayOfWeek ,  LocalTime time,
                                          String salle) {
        super(présenceDéjàEnregistrée);
        this.date = date;
        this.intitule = intitule;
        this.dayOfWeek = dayOfWeek;
        this.salle = salle;
        this.time = time;
    }

}
