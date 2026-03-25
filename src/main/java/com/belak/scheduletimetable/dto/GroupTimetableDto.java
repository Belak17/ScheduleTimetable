package com.belak.scheduletimetable.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class GroupTimetableDto {
    private Long id ;
    private String department ;
    private String field ;
    private  String group ;
    private int year ;

}
