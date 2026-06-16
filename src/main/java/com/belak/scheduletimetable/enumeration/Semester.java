package com.belak.scheduletimetable.enumeration;

import java.time.LocalDate;
import java.time.Month;

public enum Semester {
    S1, S2;

    public static Semester fromDate(LocalDate date) {
        Month m = date.getMonth();

        return switch (m) {
            case SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER, JANUARY -> S1;
            default -> S2;
        };
    }
}
