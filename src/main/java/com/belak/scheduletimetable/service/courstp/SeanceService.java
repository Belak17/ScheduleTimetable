package com.belak.scheduletimetable.service.courstp;

import com.belak.scheduletimetable.model.CoursTP;
import com.belak.scheduletimetable.model.Seance;
import com.belak.scheduletimetable.repository.CoursTPRepository;
import com.belak.scheduletimetable.repository.SeanceRepository;
import com.belak.scheduletimetable.service.presence.AbsenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private  final AbsenceService absenceService ;

    @Transactional
    @Scheduled(cron = "0 20 7 * * *", zone = "Africa/Tunis")
    public void generateDailySeances() {
        createSeancesForToday();
    }
    public void createSeancesForToday() {
        LocalDate today = LocalDate.now(ZoneId.of("Africa/Tunis"));
        LocalTime now = LocalTime.now(ZoneId.of("Africa/Tunis"));
        LocalTime end = now.plusMinutes(15);
        int weekNumber = today.get(WeekFields.ISO.weekOfWeekBasedYear());
        String todayDay = today
                .getDayOfWeek()
                .getDisplayName(TextStyle.FULL, Locale.FRANCE);

        todayDay = todayDay.substring(0, 1).toUpperCase() + todayDay.substring(1);
        List<CoursTP> nearCoursTPlist =tpRepository
                .findCoursTPByDay(todayDay);
        for (CoursTP cours : nearCoursTPlist)
        {
            System.out.println(
                    "ID=" + cours.getId()
                            + " frequence=" + cours.getFrequence()
                            + " rotationOffset=" + cours.getRotationOffset()
            );
            if (!cours.shouldOccurThisWeek(weekNumber)) {
                continue;
            }
            boolean exists = seanceRepository.existsByCoursTPAndDate(cours, today);
            if (!exists) {
                Seance seance = new Seance();
                seance.setDate(today);
                cours.addSeance(seance);
                tpRepository.save(cours);
            }
        }
    }
    @Transactional
    @Scheduled(fixedRate = 60000) // chaque minute
    public void checkSeances() {
        processFinishedSeances();
    }
    public void processFinishedSeances() {
        LocalTime now = LocalTime.now();
        List<Seance> seances =
                seanceRepository.findByEndTimeBeforeAndAbsencesProcessedFalse(now);
        for (Seance seance : seances) {
            // 1. générer les absences
            absenceService.generateAbsences(seance);
            // 2. MARQUER comme traité
            seance.setAbsencesProcessed(true);
        }
        // 3. sauvegarder les changements
        seanceRepository.saveAll(seances);
    }
    
}
