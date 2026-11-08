package com.belak.scheduletimetable.service.courstp;

import com.belak.scheduletimetable.component.SeanceDtoMapper;
import com.belak.scheduletimetable.dto.coursTP.PlanningSeanceDTO;
import com.belak.scheduletimetable.dto.coursTP.SeanceDto;
import com.belak.scheduletimetable.model.CoursTP;
import com.belak.scheduletimetable.model.Seance;
import com.belak.scheduletimetable.repository.coursTP.CoursTPRepository;
import com.belak.scheduletimetable.repository.coursTP.SeanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor
public class SeanceUService {

    private  final SeanceRepository seanceRepository ;
    private  final CoursTPRepository tpRepository ;
    private  final SeanceDtoMapper seanceDtoMapper;

    @Transactional(readOnly = true)
    public List<PlanningSeanceDTO> getExpectedSeances(LocalDate date) {

        int weekNumber = date.get(WeekFields.ISO.weekOfWeekBasedYear());

        String day = date.getDayOfWeek()
                .getDisplayName(TextStyle.FULL, Locale.FRANCE);

        day = day.substring(0, 1).toUpperCase() + day.substring(1);

        List<CoursTP> coursList = tpRepository.findCoursTPByDay(day);

        return coursList.stream()
                .filter(cours -> {

                    if (cours.getDependsOnCoursId() != null) {

                        CoursTP parent = tpRepository
                                .findById(cours.getDependsOnCoursId())
                                .orElse(null);

                        return parent != null &&
                                !parent.shouldOccurThisWeek(weekNumber);
                    }

                    return cours.shouldOccurThisWeek(weekNumber);
                })
                .map(cours -> {

                    boolean generated = seanceRepository
                            .existsByCoursTPAndDate(cours, date);

                    Seance seance = cours.getSeances()
                            .stream()
                            .filter(s -> s.getDate().equals(date))
                            .findFirst()
                            .orElse(null);

                    return new PlanningSeanceDTO(
                            cours.getId(),
                            cours.getIntitule(),
                            cours.getGroupTimetable().getEntirelyGroupName(),
                            cours.getSalle() != null ? cours.getSalle().getCode() : "NA",
                            date,
                            cours.getDebut(),
                            cours.getFin(),
                            generated,
                            seance != null && seance.isAbsencesProcessed()
                    );
                })
                .toList();
    }

    public List<SeanceDto> getGeneratedSeances(int days) {

        LocalDate today = LocalDate.now(ZoneId.of("Africa/Tunis"));

        return seanceRepository.findByDateBetweenOrderByDateDesc(
                        today.minusDays(days),
                        today
                )
                .stream()
                .map(seanceDtoMapper::toDto)
                .toList();
    }
}
