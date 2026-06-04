package com.belak.scheduletimetable.service.courstp;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Service
@RequiredArgsConstructor
public class CoursTPService extends CoursTPUtilsService {
    private final CoursTPRepository coursTPrepository;
    private  final GroupTimetableRepository timetableRepository ;
    private  final StudentRepository studentRepository;
    private  final PresenceRepository presenceRepository ;
    private final SalleRepository salleRepository;

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

    private CoursTP buildTP(String value, String dayRaw, String start, String end ,GroupTimetable groupTimetable) {
        CoursTP tp = new CoursTP();
        tp.setIntitule(value);
        tp.setDayOfWeek(dayRaw);
        tp.setDebut(LocalTime.parse(start));
        tp.setFin(LocalTime.parse(end));
        tp.setSalle(extractGroupSalle(value));
        boolean exists = coursTPrepository.existsCoursTPByDayAndHoraireAndGroupTimetable(
                dayRaw,
                LocalTime.parse(start),
                LocalTime.parse(end),groupTimetable
        );

        String frequency = extractTPFrequency(value);

        System.out.println("Cours : " + value);
        System.out.println("Frequency extraite : " + frequency);
        System.out.println("Exists : " + exists);

        if (exists) {
            System.out.println("=> fréquence 2 à cause de exists");
            tp.setFrequence(2);
        } else if (frequency == null) {
            System.out.println("=> fréquence 1");
            tp.setFrequence(1);
        } else if (frequency.contains("15")) {
            System.out.println("=> fréquence 2 à cause du texte");
            tp.setFrequence(2);
        } else if (frequency.contains("3s")) {
            System.out.println("=> fréquence 3");
            tp.setFrequence(3);
        } else {
            System.out.println("=> fréquence 1");
            tp.setFrequence(1);
        }

        return tp;
    }
    public void extractAllCoursTPforOneGroup(Worksheet sheet, GroupTimetable timetable) {

        for (int row = 10; row <= 15; row++) {

            String hourRaw = sheet.getCellRange(row, 4).getValue();
            if (hourRaw == null || !hourRaw.contains("à")) continue;

            hourRaw = hourRaw.replace("de", "").trim();

            String[] parts = hourRaw.split("à");
            String start = parts[0].trim();
            String end = parts[1].trim();

            for (int col = 5; col <= 15; col++) {

                String value = sheet.getCellRange(row, col).getValue();
                if (value == null || value.isEmpty()) continue;

                String dayRaw = sheet.getCellRange(9, col).getValue();
                if (dayRaw == null || dayRaw.isBlank()) {
                    dayRaw = sheet.getCellRange(10, col).getValue();
                }

                if (value.contains("TP")) {
                    List<String> courses = extractCourses(value);

                    //  : plusieurs TP dans la cellule
                    if (courses.size() > 1) {

                        for (int j = 0; j < courses.size(); j++) {

                            CoursTP tp = buildTP(courses.get(j), dayRaw, start, end , timetable);
                            tp.setRotationOffset(j); // 0,1 (plus logique que j+1)

                            coursTPrepository.save(tp);
                            timetable.addCoursTP(tp);
                        }
                    }
                    else {
                        CoursTP tp = buildTP(value, dayRaw, start, end, timetable);
                        coursTPrepository.save(tp);
                        timetable.addCoursTP(tp);
                    }
                }
            }
        }
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
    public AttendanceDto getAllDatesAndAttendanceByCoursTP(Long id  , int page , int size ) {

        CoursTP tp = coursTPrepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("coursTP not found"));
        AttendanceDto attendanceDto = new AttendanceDto();
        List<Seance> sortedSeances = tp.getSeances()
                .stream()
                .sorted(Comparator.comparing(Seance::getDate))
                .toList();
        attendanceDto.setDates(
                sortedSeances
                        .stream()
                        .map(Seance::getDate)
                        .toList()
        );
        List<Student> students =
                tp.getGroupTimetable().getStudents();
        int start = page * size;
        int end = Math.min(start + size, students.size());
        List<Student> paginatedStudents =
                students.subList(start, end);
        List<StudentAttendanceDto> rows = new ArrayList<>();
        for (Student student : paginatedStudents) {

            StudentAttendanceDto studentAttendanceDto =
                    new StudentAttendanceDto();

            studentAttendanceDto.setStudentName(
                    student.getNom() + " " + student.getPrenom()
            );

            for (Seance seance : sortedSeances) {

                java.util.Optional<Presence> optionalPresence =
                        presenceRepository.findBySeanceIdAndStudentId(
                                seance.getId(),
                                student.getId()
                        );

                if (optionalPresence.isPresent()) {

                    Presence presence = optionalPresence.get();

                    if (presence.getPresent()) {
                        studentAttendanceDto.addStatuses("Present");
                    } else {
                        studentAttendanceDto.addStatuses("Absent");
                    }

                } else {

                    studentAttendanceDto.addStatuses("-");

                }
            }

            rows.add(studentAttendanceDto);
        }

        attendanceDto.setRows(rows);

        return attendanceDto;
    }
}