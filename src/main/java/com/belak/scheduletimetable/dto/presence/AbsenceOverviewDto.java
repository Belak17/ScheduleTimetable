package com.belak.scheduletimetable.dto.presence;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class AbsenceOverviewDto {
    private LocalDate presentDate;
    private String presence ;
}
