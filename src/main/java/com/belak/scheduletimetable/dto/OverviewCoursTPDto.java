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
    private  Long id;
    private  String title ;
    private  int absences ;
    private  int totalSeances ;
    private  float presenceRate ;

    public OverviewCoursTPDto(Long id , String intitule, int totalAbsences, int totalSeances) {
        this.id = id;
        this.title=intitule;
        this.totalSeances= totalSeances;
        this.absences= totalAbsences;
        calculPresenceRate();
    }

    public void calculPresenceRate()
    {
        if (totalSeances==0)
        {
            this.presenceRate = 0.0f;
        }
        else {
            this.presenceRate = ((float) (totalSeances - absences) / totalSeances) * 100;
        }
    }
}
