package com.belak.scheduletimetable.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class OverviewCoursTPDto {
    private  String title ;
    private  int absences ;
    private  int totalSeances ;
    private  float presenceRate ;

    public OverviewCoursTPDto(String intitule, int totalAbsences, int totalSeances) {
        this.title=intitule;
        this.totalSeances= totalSeances;
        this.absences= totalAbsences;
        calculPresenceRate();
    }

    public void calculPresenceRate()
    {
        this.presenceRate=  ((float) (totalSeances - absences) /totalSeances)*100 ;
    }
}
