package com.belak.scheduletimetable.service.courstp;

import com.belak.scheduletimetable.dto.AttendancePageDto;
import com.belak.scheduletimetable.dto.CoursTPDto;
import com.belak.scheduletimetable.dto.AttendanceDto;
import com.belak.scheduletimetable.dto.StudentAttendanceDto;
import com.belak.scheduletimetable.enumeration.Departement;
import com.belak.scheduletimetable.enumeration.Filiere;
import com.belak.scheduletimetable.exception.ResourceNotFoundException;
import com.belak.scheduletimetable.model.*;
import com.belak.scheduletimetable.repository.*;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.spire.xls.Worksheet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Slf4j
@Service
@RequiredArgsConstructor
public class CoursTPService extends CoursTPUtilsService {
    private final CoursTPRepository coursTPrepository;
    private  final GroupTimetableRepository timetableRepository ;
    private  final StudentRepository studentRepository;
    private  final PresenceRepository presenceRepository ;
    private final SalleRepository salleRepository;
    private final SeanceRepository seanceRepository;

    private static final Logger logger =
            LoggerFactory.getLogger(CoursTPService.class);

    public Salle extractGroupSalle(String text) {

        if (text == null) return null;

        List<Salle> salles = salleRepository.findAll();

        for (Salle salle : salles) {

            String code = salle.getCode();

            // séparation pour éviter S1 == S10
            if (text.matches(".*\\b" + Pattern.quote(code) + "\\b.*")) {
                return salle;
            }
        }

        return null;
    }

    private CoursTP buildTP(
            String value,
            String dayRaw,
            String start,
            String end,
            GroupTimetable groupTimetable
    ) {
        CoursTP tp = new CoursTP();

        tp.setGroupTimetable(groupTimetable);

        tp.setIntitule(value);
        tp.setDayOfWeek(dayRaw);
        tp.setDebut(LocalTime.parse(start));
        tp.setFin(LocalTime.parse(end));
        tp.setSalle(extractGroupSalle(value));

        boolean exists = coursTPrepository.existsCoursTPByDayAndHoraireAndGroupTimetable(
                dayRaw,
                LocalTime.parse(start),
                LocalTime.parse(end),
                groupTimetable
        );

        String frequency = extractTPFrequency(value);

        //log.info(
          //      "Le cours TP intitule {} a pour frequence {}",
            //    value,
              //  frequency
        //);

        //log.info(
          //      "Existence de ce cours dans la base de donnee {}",
            //    exists
        //);

        if (exists) {
            //log.info("=> fréquence 2 à cause de exists");
            tp.setFrequence(2);
        } else if (frequency == null) {
            //log.info("=> fréquence 1");
            tp.setFrequence(1);
        } else if (frequency.contains("15")) {
            //log.info("=> fréquence 2 à cause du texte");
            tp.setFrequence(2);
        } else if (frequency.contains("3s")) {
            //log.info("=> fréquence 3");
            tp.setFrequence(3);
        } else {
            //log.info("=> fréquence 1");
            tp.setFrequence(1);
        }

        return tp;
    }
    public void extractAllCoursTPforOneGroup(Worksheet sheet, GroupTimetable timetable) {

        for (int row = 10; row <= 15; row++) {

            String hourRaw = sheet.getCellRange(row, 4).getValue();

            if (hourRaw == null || !hourRaw.contains("à")) {
                continue;
            }

            hourRaw = hourRaw.replace("de", "").trim();

            String[] parts = hourRaw.split("à");

            String start = parts[0].trim();
            String end = parts[1].trim();

            for (int col = 5; col <= 15; col++) {

                String value = sheet.getCellRange(row, col).getValue();

                if (value == null || value.isBlank()) {
                    continue;
                }

                String dayRaw = sheet.getCellRange(9, col).getValue();

                if (dayRaw == null || dayRaw.isBlank()) {
                    dayRaw = sheet.getCellRange(10, col).getValue();
                }

                if (!value.contains("TP")) {
                    continue;
                }

                List<String> courses = extractCourses(value);

                if (courses.size() > 1) {

                    for (int j = 0; j < courses.size(); j++) {

                        CoursTP tp = buildTP(
                                courses.get(j),
                                dayRaw,
                                start,
                                end,
                                timetable
                        );

                        int freq = tp.getFrequence();

                        if (freq <= 0) {
                            log.warn(
                                    "Fréquence invalide pour le cours : {}",
                                    tp.getIntitule()
                            );

                            throw new IllegalArgumentException(
                                    "Fréquence invalide pour le cours : "
                                            + tp.getIntitule()
                            );
                        }

                        tp.setRotationOffset(j % freq);

                        // La relation CoursTP -> GroupTimetable
                        // est déjà définie dans buildTP().
                        // Ici, on synchronise également le côté GroupTimetable.
                        timetable.addCoursTP(tp);
                    }

                } else {

                    CoursTP tp = buildTP(
                            value,
                            dayRaw,
                            start,
                            end,
                            timetable
                    );

                    timetable.addCoursTP(tp);
                }
            }
        }

        // CascadeType.ALL persiste les CoursTP associés au timetable
        timetableRepository.save(timetable);
    }
    public Page<CoursTPDto> getAllCoursTPByGroupTimetable(int page , int size ,String departement , String group ,
                                                          int niveau , String filiere )
    {
        Pageable pageable = PageRequest.of(page, size);
        return  coursTPrepository
                .findByGroupTimetableId(pageable , timetableRepository
                        .findByDepartementAndFiliereAndGroupAndNiveau( Departement
                                .valueOf(departement), Filiere.fromCode(filiere),group,niveau)
                        .getId()).map(this::toDto);
    }

    public CoursTPDto toDto(CoursTP tp) {
        CoursTPDto dto = new CoursTPDto();
        dto.setId(tp.getId());
        dto.setIntitule(tp.getIntitule());
        dto.setDayOfWeek(tp.getDayOfWeek());
        dto.setDebut(tp.getDebut());
        dto.setFin(tp.getFin());
        return dto;
    }
    @Transactional(readOnly = true)
    public AttendancePageDto getAllDatesAndAttendanceByCoursTP(
            Long id, int page, int size) {

        // 1. Récupération du cours TP
        CoursTP tp = coursTPrepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cours TP introuvable"));

        // 2. Initialisation des DTO
        AttendanceDto attendanceDto = new AttendanceDto();
        AttendancePageDto pageDto = new AttendancePageDto();

        // 3. Validation des paramètres de pagination
        if (page < 0) {
            page = 0;
        }

        if (size <= 0) {
            size = 10;
        }

        // 4. Récupération des séances triées depuis la base
        List<Seance> sortedSeances =
                seanceRepository.findByCoursTP_IdOrderByDateAsc(id);

        attendanceDto.setDates(
                sortedSeances.stream()
                        .map(Seance::getDate)
                        .toList()
        );

        // 5. Pagination SQL des étudiants
        Long groupTimetableId = tp.getGroupTimetable().getId();

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Order.asc("nom"),
                        Sort.Order.asc("id")
                )
        );

        Page<Student> studentPage =
                studentRepository.findByGroupId(groupTimetableId, pageable);

        // 6. Informations de pagination
        pageDto.setTotalStudents(studentPage.getTotalElements());
        pageDto.setTotalPages(studentPage.getTotalPages());
        pageDto.setCurrentPage(page);
        pageDto.setPageSize(size);

        List<Student> paginatedStudents = studentPage.getContent();

        // 7. Aucun étudiant sur cette page
        if (paginatedStudents.isEmpty()) {
            attendanceDto.setRows(Collections.emptyList());
            pageDto.setAttendanceTable(attendanceDto);
            return pageDto;
        }

        // 8. Récupération des identifiants
        List<Long> studentIds = paginatedStudents.stream()
                .map(Student::getId)
                .toList();

        List<Long> seanceIds = sortedSeances.stream()
                .map(Seance::getId)
                .toList();

        // 9. Récupération groupée des présences
        Map<String, Presence> presenceMap = new HashMap<>();

        if (!seanceIds.isEmpty()) {

            List<Presence> presences =
                    presenceRepository.findAllBySeanceIdsAndStudentIds(
                            seanceIds,
                            studentIds
                    );

            for (Presence presence : presences) {

                String key = presence.getStudent().getId()
                        + "_" + presence.getSeance().getId();

                presenceMap.put(key, presence);
            }
        }

        // 10. Construction des lignes du tableau
        List<StudentAttendanceDto> rows = new ArrayList<>();

        for (Student student : paginatedStudents) {

            StudentAttendanceDto row = new StudentAttendanceDto();

            row.setStudentName(student.getNom() + " " + student.getPrenom());
            row.setId(student.getUserId());

            for (Seance seance : sortedSeances) {

                String key = student.getId() + "_" + seance.getId();

                Presence presence = presenceMap.get(key);

                if (presence == null) {
                    row.addStatuses("-");
                } else if (presence.isPresent()) {
                    row.addStatuses("Present");
                } else {
                    row.addStatuses("Absent");
                }
            }

            rows.add(row);
        }

        // 11. Assemblage des DTO
        attendanceDto.setRows(rows);
        pageDto.setAttendanceTable(attendanceDto);

        return pageDto;
    }
}