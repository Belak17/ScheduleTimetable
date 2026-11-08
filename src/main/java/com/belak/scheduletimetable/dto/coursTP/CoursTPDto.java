package com.belak.scheduletimetable.dto.coursTP;

import lombok.Data;

import java.time.LocalTime;

@Data
public class CoursTPDto {
    private  Long id ;
    private  String intitule ;
    private String dayOfWeek ;
    private LocalTime debut ;
    private LocalTime fin ;
}
