package com.belak.scheduletimetable.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
public class PresenceDto {
    private LocalDate date ;
    private String intitule ;
}
