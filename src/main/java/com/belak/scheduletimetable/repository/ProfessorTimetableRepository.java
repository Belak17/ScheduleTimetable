package com.belak.scheduletimetable.repository;

import com.belak.scheduletimetable.model.ProfessorTimetable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorTimetableRepository extends JpaRepository<ProfessorTimetable,Long> {

}
