package com.belak.scheduletimetable.repository;

import com.belak.scheduletimetable.enumeration.Departement;
import com.belak.scheduletimetable.enumeration.Filiere;
import com.belak.scheduletimetable.model.GroupTimetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface GroupTimetableRepository  extends JpaRepository<GroupTimetable,Long> {
    @Query("SELECT DISTINCT g.filiere FROM GroupTimetable g WHERE g.departement = :departement")
    List<Filiere> findDistinctFilieresByDepartement(@Param("departement") Departement departement);
}
