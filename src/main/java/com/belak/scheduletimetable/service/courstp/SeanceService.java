package com.belak.scheduletimetable.service.courstp;

import com.belak.scheduletimetable.model.CoursTP;
import com.belak.scheduletimetable.model.Seance;
import com.belak.scheduletimetable.repository.CoursTPRepository;
import com.belak.scheduletimetable.repository.SeanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class SeanceService {
    private  final SeanceRepository seanceRepository ;
    private  final CoursTPRepository tpRepository ;

    @Scheduled(cron = "0 5 8 * * MON-SAT", zone = "Africa/Tunis")
    public void job1() {
        createAllSeanceTP();
    }
    @Scheduled(cron = "0 45 9 * * MON-SAT", zone = "Africa/Tunis")
    public void job2() {
        createAllSeanceTP();
    }
    @Scheduled(cron = "0 25 11 * * MON-SAT", zone = "Africa/Tunis")
    public void job3() {
        createAllSeanceTP();
    }

    @Scheduled(cron = "0 0 13 * * MON-SAT", zone = "Africa/Tunis")
    public void job4() {
        createAllSeanceTP();
    }

    @Scheduled(cron = "0 40 14 * * MON-SAT", zone = "Africa/Tunis")
    public void job5() {
        createAllSeanceTP();
    }

    @Scheduled(cron = "0 20 16 * * MON-SAT", zone = "Africa/Tunis")
    public void job6() {
        createAllSeanceTP();
    }
    public void createAllSeanceTP() {
        LocalDate today = LocalDate.now(ZoneId.of("Africa/Tunis"));
        LocalTime now = LocalTime.now(ZoneId.of("Africa/Tunis"));
        LocalTime end = now.plusMinutes(15);

        int weekNumber = today.get(WeekFields.ISO.weekOfWeekBasedYear());

        String todayDay =today
                .getDayOfWeek()
                .getDisplayName(TextStyle.FULL, Locale.FRANCE);
        List<CoursTP> nearCoursTPlist =tpRepository
                .findCoursTPByDayAndHoraire(now,end,todayDay);

        for (CoursTP cours : nearCoursTPlist)
        {
            if (!cours.shouldOccurThisWeek(weekNumber)) {
                continue;
            }

            boolean exists = seanceRepository.existsByCoursTPAndDate(cours, today);

            if (!exists) {
                Seance seance = new Seance();
                seance.setDate(LocalDate.now());
                cours.addSeance(seance);
                tpRepository.save(cours);
            }
        }

    }


}
