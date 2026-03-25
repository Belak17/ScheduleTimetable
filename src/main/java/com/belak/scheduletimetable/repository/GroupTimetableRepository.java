package com.belak.scheduletimetable.repository;

import com.belak.scheduletimetable.model.GroupTimetable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GroupTimetableRepository  extends JpaRepository<GroupTimetable,Long> {
}
