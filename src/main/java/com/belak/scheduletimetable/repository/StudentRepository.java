package com.belak.scheduletimetable.repository;

import com.belak.scheduletimetable.enumeration.Departement;
import com.belak.scheduletimetable.enumeration.Filiere;
import com.belak.scheduletimetable.model.Presence;
import com.belak.scheduletimetable.model.Professor;
import com.belak.scheduletimetable.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Long> {


    Page<Student> findByFiliereAndNiveauAndGroup(Filiere field, int year, String group, Pageable pageable);

    List<Student> findByDepartmentAndFiliereAndNiveauAndGroup(Departement departement, Filiere filiere, int year, String group);

    Optional<Student> findByUserId(String userId);

    @Query("""
SELECT s FROM Student s
LEFT JOIN FETCH s.timetables t
WHERE s.userId = :userId
""")
    Optional<Student> findByUserIdWithTimetable(@Param("userId") String userId);

    void deleteByUserId(String userId);

    @Query("SELECT s FROM Student s JOIN s.timetables t WHERE t.id = :timetableId")
    List<Student> findByGroupId(@Param("timetableId") Long groupId);


}
