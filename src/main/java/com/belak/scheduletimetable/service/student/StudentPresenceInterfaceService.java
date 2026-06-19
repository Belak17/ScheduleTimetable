package com.belak.scheduletimetable.service.student;

import com.belak.scheduletimetable.dto.AbsenceOverviewDto;
import com.belak.scheduletimetable.dto.OverviewCoursTPDto;
import com.belak.scheduletimetable.enumeration.Semester;
import com.belak.scheduletimetable.model.CoursTP;
import com.belak.scheduletimetable.model.Seance;
import com.belak.scheduletimetable.model.Student;

import java.util.List;

public interface StudentPresenceInterfaceService {
    public List<CoursTP> getCoursTPByUserId(String userId);

    public boolean isPresent(Student student, Seance seance);

    public int getTotalSeancesByCoursTP(CoursTP coursTP);

    public List<OverviewCoursTPDto> getAllStudentOverviewByStudent(Student student);

    public int getTotalAbsencesByCoursTP(CoursTP coursTP, Student student);

    public List<OverviewCoursTPDto> getAllStudentOverviewByUserId(String userId, Semester semester);

    public OverviewCoursTPDto getOverviewByUserIdAndCoursTP(CoursTP coursTP, Student student);

    public List<AbsenceOverviewDto> getAllAbsenceByUserIdAndCoursTP(String userId, Long coursTPId);

}