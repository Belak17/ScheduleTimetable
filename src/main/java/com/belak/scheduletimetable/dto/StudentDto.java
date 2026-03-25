package com.belak.scheduletimetable.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class StudentDto {
    private String userId ;
    private String department ;
    private  String field ;
    private  String group ;
    private int year ;
    private String nom ;
    private String prenom ;
    private String email ;


}
