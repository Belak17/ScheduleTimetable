package com.belak.scheduletimetable.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class AttendanceDto {

    private List<LocalDate> dates = new ArrayList<>();

    private  List<StudentAttendanceDto> rows;

    public void addDates(LocalDate date) {
        if (dates==null)
        {
            dates = new ArrayList<>();
        }
        this.dates.add(date);
    }

    public void addStudentAttendanceDto(StudentAttendanceDto studentAttendanceDTO) {
        if (rows==null)
        {
            rows = new ArrayList<>();
        }
        this.rows.add(studentAttendanceDTO);
    }

}
