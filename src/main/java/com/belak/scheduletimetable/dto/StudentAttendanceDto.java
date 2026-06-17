package com.belak.scheduletimetable.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class StudentAttendanceDto {

    private  String id ;
    private String studentName;

    private List<String> statuses;

    public void addStatuses(String status) {
        if (statuses==null)
        {
            statuses = new ArrayList<>();
        }
        this.statuses.add(status);
    }
}
