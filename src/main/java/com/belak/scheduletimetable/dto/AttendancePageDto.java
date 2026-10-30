package com.belak.scheduletimetable.dto;

import lombok.Data;

@Data
public class AttendancePageDto {

    private AttendanceDto attendanceTable;

    private long totalStudents;

    private int totalPages;

    private int currentPage;

    private int pageSize;
}
