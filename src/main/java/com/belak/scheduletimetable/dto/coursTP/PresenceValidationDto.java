package com.belak.scheduletimetable.dto.coursTP;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

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
    private String nom ;

    private String prenom ;
}
