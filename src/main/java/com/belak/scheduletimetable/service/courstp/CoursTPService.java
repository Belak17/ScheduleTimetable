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
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

        log.info(
                "Le cours TP intitule {} a pour frequence {}",
                value,
                frequency
        );

        log.info(
                "Existence de ce cours dans la base de donnee {}",
                exists
        );

        if (exists) {
            log.info("=> fréquence 2 à cause de exists");
            tp.setFrequence(2);
        } else if (frequency == null) {
            log.info("=> fréquence 1");
            tp.setFrequence(1);
        } else if (frequency.contains("15")) {
            log.info("=> fréquence 2 à cause du texte");
            tp.setFrequence(2);
        } else if (frequency.contains("3s")) {
            log.info("=> fréquence 3");
            tp.setFrequence(3);
        } else {
            log.info("=> fréquence 1");
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
    public AttendanceDto getAllDatesAndAttendanceByCoursTP(
            Long id,
            int page,
            int size) {

        logger.info("========== DÉBUT getAllDatesAndAttendanceByCoursTP ==========");
        logger.info("Cours TP ID reçu : {}", id);
        logger.info("Paramètres initiaux : page={}, size={}", page, size);

        // 1. Récupérer le cours TP
        CoursTP tp = coursTPrepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Cours TP introuvable pour l'ID : {}", id);
                    return new ResourceNotFoundException("Cours TP introuvable");
                });

        logger.info("Cours TP récupéré avec succès : {}", tp.getId());

        AttendanceDto attendanceDto = new AttendanceDto();

        // 2. Récupérer et trier les séances
        List<Seance> sortedSeances = tp.getSeances()
                .stream()
                .sorted(Comparator.comparing(Seance::getDate))
                .toList();

        logger.info("Nombre de séances récupérées : {}", sortedSeances.size());

        sortedSeances.forEach(seance ->
                logger.debug(
                        "Séance ID={}, date={}",
                        seance.getId(),
                        seance.getDate()
                )
        );

        attendanceDto.setDates(
                sortedSeances.stream()
                        .map(Seance::getDate)
                        .toList()
        );

        logger.info(
                "Nombre de dates transmises au DTO : {}",
                attendanceDto.getDates().size()
        );

        // 3. Vérifier le groupe avant de récupérer ses étudiants
        if (tp.getGroupTimetable() == null) {
            logger.error("Aucun groupe associé au cours TP {}", id);
            throw new ResourceNotFoundException(
                    "Aucun groupe associé à ce cours TP"
            );
        }

        logger.info(
                "Groupe associé au cours TP : {}",
                tp.getGroupTimetable().getId()
        );

        // 4. Récupérer les étudiants du groupe
        logger.info("Récupération des étudiants du groupe...");

        List<Student> students = tp.getGroupTimetable()
                .getStudents()
                .stream()
                .toList();

        logger.info("Nombre total d'étudiants récupérés : {}", students.size());

        students.forEach(student ->
                logger.debug(
                        "Étudiant ID={}, userId={}, nom={}, prénom={}",
                        student.getId(),
                        student.getUserId(),
                        student.getNom(),
                        student.getPrenom()
                )
        );

        // 5. Vérifier les paramètres de pagination
        if (page < 0) {
            logger.warn("Page négative détectée : {}. Réinitialisation à 0.", page);
            page = 0;
        }

        if (size <= 0) {
            logger.warn("Taille invalide : {}. Réinitialisation à 10.", size);
            size = 10;
        }

        logger.info(
                "Paramètres de pagination utilisés : page={}, size={}",
                page,
                size
        );

        // 6. Calculer les indices de pagination
        int start = page * size;

        logger.info(
                "Calcul pagination : start={}, totalStudents={}",
                start,
                students.size()
        );

        if (start >= students.size()) {
            logger.warn(
                    "Aucun étudiant sur cette page : start={} >= total={}",
                    start,
                    students.size()
            );

            attendanceDto.setRows(Collections.emptyList());

            logger.info("========== FIN getAllDatesAndAttendanceByCoursTP ==========");
            return attendanceDto;
        }

        int end = Math.min(start + size, students.size());

        logger.info("Indices de pagination : start={}, end={}", start, end);

        List<Student> paginatedStudents = students.subList(start, end);

        logger.info(
                "Nombre d'étudiants après pagination : {}",
                paginatedStudents.size()
        );

        // 7. Initialiser la liste des présences
        Map<String, Presence> presenceMap = new HashMap<>();

        // Récupérer les présences seulement s'il existe des étudiants
        // et au moins une séance.
        if (!paginatedStudents.isEmpty() && !sortedSeances.isEmpty()) {

            List<Long> studentIds = paginatedStudents.stream()
                    .map(Student::getId)
                    .toList();

            List<Long> seanceIds = sortedSeances.stream()
                    .map(Seance::getId)
                    .toList();

            logger.info("Identifiants des étudiants de la page : {}", studentIds);
            logger.info("Identifiants des séances : {}", seanceIds);

            // 8. Récupérer toutes les présences en une requête
            logger.info("Recherche des présences en base de données...");

            List<Presence> presences =
                    presenceRepository.findAllBySeanceIdsAndStudentIds(
                            seanceIds,
                            studentIds
                    );

            logger.info(
                    "Nombre de présences récupérées : {}",
                    presences.size()
            );

            // 9. Indexer les présences
            for (Presence presence : presences) {

                String key = presence.getStudent().getId()
                        + "_" + presence.getSeance().getId();

                presenceMap.put(key, presence);

                logger.debug(
                        "Présence : étudiant={}, séance={}, présent={}, clé={}",
                        presence.getStudent().getId(),
                        presence.getSeance().getId(),
                        presence.isPresent(),
                        key
                );
            }

            logger.info("Taille de la map des présences : {}", presenceMap.size());

        } else if (sortedSeances.isEmpty()) {

            logger.info(
                    "Aucune séance pour le cours TP {}. " +
                            "Les étudiants seront affichés sans colonnes de présence.",
                    id
            );
        }

        // 10. Construire les lignes des étudiants
        // IMPORTANT : cette partie s'exécute même si aucune séance n'existe.
        List<StudentAttendanceDto> rows = new ArrayList<>();

        for (Student student : paginatedStudents) {

            StudentAttendanceDto row = new StudentAttendanceDto();

            row.setStudentName(
                    student.getNom() + " " + student.getPrenom()
            );

            row.setId(student.getUserId());

            logger.debug(
                    "Construction de la ligne pour {} {}",
                    student.getNom(),
                    student.getPrenom()
            );

            // Les statuts sont ajoutés uniquement pour les séances existantes.
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

            logger.debug(
                    "Ligne créée : étudiant={}, nombre de statuts={}",
                    row.getStudentName(),
                    row.getStatuses() != null
                            ? row.getStatuses().size()
                            : 0
            );

            rows.add(row);
        }

        logger.info("Nombre de lignes construites : {}", rows.size());

        // 11. Retourner le résultat
        attendanceDto.setRows(rows);

        logger.info(
                "Nombre de lignes dans le DTO final : {}",
                attendanceDto.getRows().size()
        );

        logger.info("========== FIN getAllDatesAndAttendanceByCoursTP ==========");

        return attendanceDto;
    }
}