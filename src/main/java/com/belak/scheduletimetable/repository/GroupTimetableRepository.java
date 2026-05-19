package com.belak.scheduletimetable.repository;

import com.belak.scheduletimetable.enumeration.Departement;
import com.belak.scheduletimetable.enumeration.Filiere;
import com.belak.scheduletimetable.model.GroupTimetable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import org.springframework.data.domain.Page;

public interface GroupTimetableRepository  extends JpaRepository<GroupTimetable,Long> {
    @Query("SELECT DISTINCT g.filiere FROM GroupTimetable g WHERE g.departement = :departement")
    List<Filiere> findDistinctFilieresByDepartement(@Param("departement") Departement departement);

    Page<GroupTimetable> findByDepartement(Pageable pageable, Departement departement);

    @Query("""
    SELECT DISTINCT g.group
    FROM GroupTimetable g
    WHERE g.departement = :departement
    AND g.filiere = :filiere
    AND g.niveau = :niveau
""")
    List<String> findDistinctGroupByDepartementAndFiliereAndNiveau( @Param("departement") Departement departement,
                                                                    @Param("filiere") Filiere filiere,
                                                                    @Param("niveau") int niveau);

    GroupTimetable findByDepartementAndFiliereAndGroupAndNiveau(Departement departement,Filiere filiere,String group, int niveau);
}
