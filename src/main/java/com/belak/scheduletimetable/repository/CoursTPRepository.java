package com.belak.scheduletimetable.repository;

import com.belak.scheduletimetable.model.CoursTP;
import com.belak.scheduletimetable.model.GroupTimetable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CoursTPRepository extends JpaRepository<CoursTP,Long> {

    @Query("SELECT cours FROM CoursTP cours " +
            "WHERE cours.debut BETWEEN :start AND :end " +
            "AND cours.dayOfWeek = :todayday")
    List<CoursTP> findCoursTPByDayAndHoraire(@Param("start") LocalTime start,
                                             @Param("end") LocalTime end,
                                             @Param("todayday") String day);


    //Optional<CoursTP> findByQrDataAndGroupTimetableIdAndDayOfWeek(String code, Long id, String todayDay);

    @Query("""
    SELECT c
    FROM CoursTP c
    WHERE c.salle.code = :code
    AND c.groupTimetable.id = :id
    AND c.dayOfWeek = :todayDay
    AND :now BETWEEN c.debut AND c.fin
""")
    Optional<CoursTP> findValidCours(
            @Param("code") String code,
            @Param("id") Long id,
            @Param("todayDay") String todayDay,
            @Param("now") LocalTime now
    );

    @Query("SELECT cours FROM CoursTP cours " +
            "WHERE cours.dayOfWeek = :todayday")
    List<CoursTP> findCoursTPByDay(@Param("todayday") String todayDay);

    @Query("""
    SELECT COUNT(c) > 0
    FROM CoursTP c
    WHERE c.debut = :start
      AND c.fin = :end
      AND c.dayOfWeek = :todayday
      AND c.groupTimetable= :groupTimetable
""")
    boolean existsCoursTPByDayAndHoraireAndGroupTimetable(
            @Param("todayday") String dayRaw,
            @Param("start") LocalTime start,
            @Param("end") LocalTime end, @Param("groupTimetable") GroupTimetable groupTimetable
    );

    @Query("""
    SELECT c
    FROM CoursTP c
    WHERE  c.groupTimetable.id = :id
""")
    Page<CoursTP> findByGroupTimetableId(Pageable pageable, @Param("id") Long id);

    @Query("""
SELECT c
FROM CoursTP c
WHERE c.groupTimetable.id = :groupId
AND c.dayOfWeek = :day
AND c.debut <= :now
AND c.fin >= :now
""")
    Optional<CoursTP> findCurrentCours(
            @Param("groupId") Long groupId,
            @Param("day") String day,
            @Param("now") LocalTime now
    );
}
