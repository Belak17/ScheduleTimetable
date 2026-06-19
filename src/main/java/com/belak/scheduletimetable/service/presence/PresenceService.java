package com.belak.scheduletimetable.service.presence;

import com.belak.scheduletimetable.component.PresenceMapper;
import com.belak.scheduletimetable.dto.PresenceDto;
import com.belak.scheduletimetable.dto.PresenceValidationDto;
import com.belak.scheduletimetable.enumeration.Semester;
import com.belak.scheduletimetable.exception.CourseAndCodeNotFoundException;
import com.belak.scheduletimetable.exception.ElementNotFoundException;
import com.belak.scheduletimetable.exception.PresenceAlreadyExistsException;
import com.belak.scheduletimetable.exception.ResourceNotFoundException;
import com.belak.scheduletimetable.model.CoursTP;
import com.belak.scheduletimetable.model.Presence;
import com.belak.scheduletimetable.model.Seance;
import com.belak.scheduletimetable.model.Student;
import com.belak.scheduletimetable.repository.CoursTPRepository;
import com.belak.scheduletimetable.repository.PresenceRepository;
import com.belak.scheduletimetable.repository.SeanceRepository;
import com.belak.scheduletimetable.repository.StudentRepository;
import com.belak.scheduletimetable.service.student.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PresenceService {
    private  final PresenceRepository presenceRepository ;
    private  final StudentRepository studentRepository ;
    private  final CoursTPRepository tpRepository ;
    private  final SeanceRepository seanceRepository ;
    private  final PresenceMapper presenceMapper ;
    private  final StudentService studentService ;

    public PresenceValidationDto createPresence(String userId , String code)
    {
        LocalDate today = LocalDate.now(ZoneId.of("Africa/Tunis"));
        LocalTime now = LocalTime.now(ZoneId.of("Africa/Tunis"));
        Student theStudent = studentService.findStudentByUserId(userId);
        String todayDay = today
                .getDayOfWeek()
                .getDisplayName(TextStyle.FULL, Locale.FRANCE);

        todayDay = todayDay.substring(0, 1).toUpperCase() + todayDay.substring(1);
        code = code.trim();
        Optional<CoursTP> optionalCoursTP= tpRepository
                .findValidCours(code,theStudent.getTimetables()
                        .stream()
                        .filter(t -> t.getSemester() == Semester.fromDate(LocalDate.now(ZoneId.of("Africa/Tunis"))))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("No timetable found for this semester "))
                        .getId(),todayDay,now);
        if (optionalCoursTP.isEmpty())
        {
            throw new CourseAndCodeNotFoundException("Cours Non disponible ",code);
        }
        CoursTP theCoursTP = optionalCoursTP.get();

        Optional<Seance> optionalSeance = seanceRepository.findByCoursTPIdAndDate(theCoursTP.getId(), today);
        if (optionalSeance.isEmpty())
        {
            throw new ElementNotFoundException("Seance Non disponible ");
        }
        Seance theSeance = optionalSeance.get();
        boolean exists = presenceRepository.existsBySeanceIdAndStudentId(
                theSeance.getId(),
                theStudent.getId()
        );
        if (exists) {
            Presence presence = presenceRepository.findBySeanceIdAndStudentId(theSeance.getId(), theStudent.getId()).get();
            throw new PresenceAlreadyExistsException("Présence déjà enregistrée",presence.getSeance()
                    .getCoursTP().getIntitule(),
                    presence.getSeance().getDate(),
                    presence.getSeance().getCoursTP().getDayOfWeek() ,
                    presence.getLocalTime() ,
                    presence.getSeance().getCoursTP().getSalle().getCode()
            );
        }

        Presence thePresence = new Presence();
        thePresence.setStudent(theStudent);
        thePresence.setPresent(true);
        thePresence.setLocalTime(
                LocalTime.now(ZoneId.of("Africa/Tunis"))
        );
        theSeance.addPresence(thePresence);

        seanceRepository.save(theSeance);

        return validatePresence(code, theStudent.getGroup(), theCoursTP.getIntitule(), todayDay) ;
    }
    public Page<PresenceDto> getAbsencesByUserId(String userId , int page , int size )
    {
        Pageable pageable = PageRequest.of(page, size);
        Student student = studentRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Etudiant non trouvé pour userId : " + userId)
                );
        return  presenceRepository.findByStudentAndPresentFalse(student,pageable).map(presenceMapper::convertPresencetoDto);
    }

    public PresenceValidationDto createPresenceWithoutCode(String userId , String code)
    {
        LocalDate today = LocalDate.now(ZoneId.of("Africa/Tunis"));
        LocalTime now = LocalTime.now(ZoneId.of("Africa/Tunis"));
        Optional<Student> student = studentRepository.findByUserId(userId);
        String todayDay = today
                .getDayOfWeek()
                .getDisplayName(TextStyle.FULL, Locale.FRANCE);

        todayDay = todayDay.substring(0, 1).toUpperCase() + todayDay.substring(1);
        if (student.isEmpty())
        {
            throw  new ResourceNotFoundException("Cet etudiant n'est pas enregistre dans la faculte");
        }
        Student theStudent = student.get();
        Optional<CoursTP> optionalCoursTP= tpRepository
                .findCurrentCours(theStudent.getTimetables()
                        .stream()
                        .filter(t -> t.getSemester() == Semester.fromDate(LocalDate.now(ZoneId.of("Africa/Tunis"))))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("No timetable found for this semester "))
                        .getId(),todayDay,now);
        if (optionalCoursTP.isEmpty())
        {
            throw new CourseAndCodeNotFoundException("Cours Non disponible ",code);
        }
        CoursTP theCoursTP = optionalCoursTP.get();

        Optional<Seance> optionalSeance = seanceRepository.findByCoursTPIdAndDate(theCoursTP.getId(), today);
        if (optionalSeance.isEmpty())
        {
            throw new ElementNotFoundException("Seance Non disponible");
        }
        Seance theSeance = optionalSeance.get();
        boolean exists = presenceRepository.existsBySeanceIdAndStudentId(
                theSeance.getId(),
                theStudent.getId()
        );
        if (exists) {
            Presence presence = presenceRepository.findBySeanceIdAndStudentId(theSeance.getId(), theStudent.getId()).get();
            throw new PresenceAlreadyExistsException("Présence déjà enregistrée",presence.getSeance()
                    .getCoursTP().getIntitule(),
                    presence.getSeance().getDate(),
                    presence.getSeance().getCoursTP().getDayOfWeek() ,
                    presence.getLocalTime() ,
                    presence.getSeance().getCoursTP().getSalle().getCode()
            );
        }
        Presence thePresence = new Presence();
        thePresence.setStudent(theStudent);
        thePresence.setPresent(true);
        thePresence.setLocalTime(
                LocalTime.now(ZoneId.of("Africa/Tunis"))
        );
        theSeance.addPresence(thePresence);

        seanceRepository.save(theSeance);

        return validatePresence(code, theStudent.getGroup(), theCoursTP.getIntitule(), todayDay) ;
    }

    public PresenceValidationDto validatePresence(String code, String group ,
                                                  String intitule, String day)
    {
        PresenceValidationDto presenceValidationDto = new PresenceValidationDto();
        presenceValidationDto.setCode(code);
        presenceValidationDto.setDate(LocalDate.now());
        presenceValidationDto.setGroup(group);
        presenceValidationDto.setIntitule(intitule);
        presenceValidationDto.setDay(day);
        presenceValidationDto.setTime(LocalTime.now(ZoneId.of("Africa/Tunis")));
        return presenceValidationDto;
    }


}
