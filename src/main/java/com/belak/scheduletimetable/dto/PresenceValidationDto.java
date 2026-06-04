package com.belak.scheduletimetable.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PresenceValidationDto {
    private  String intitule ;
    private  String group  ;
    private  String code;
    private LocalDate date;
    private LocalTime time;
    private String day ;
}
