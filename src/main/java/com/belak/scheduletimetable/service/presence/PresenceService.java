package com.belak.scheduletimetable.service.presence;

import com.belak.scheduletimetable.component.PresenceMapper;
import com.belak.scheduletimetable.dto.PresenceDto;
import com.belak.scheduletimetable.exception.ResourceNotFoundException;
import com.belak.scheduletimetable.model.CoursTP;
import com.belak.scheduletimetable.model.Presence;
import com.belak.scheduletimetable.model.Seance;
import com.belak.scheduletimetable.model.Student;
import com.belak.scheduletimetable.repository.CoursTPRepository;
import com.belak.scheduletimetable.repository.PresenceRepository;
import com.belak.scheduletimetable.repository.SeanceRepository;
import com.belak.scheduletimetable.repository.StudentRepository;
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

    public void  createPresence(String userId , String code)
    {
        LocalDate today = LocalDate.now(ZoneId.of("Africa/Tunis"));
        LocalTime now = LocalTime.now(ZoneId.of("Africa/Tunis"));
        Optional<Student> student = studentRepository.findByUserId(userId);
        String todayDay =today
                .getDayOfWeek()
                .getDisplayName(TextStyle.FULL, Locale.FRANCE);
        if (student.isEmpty())
        {
            throw  new ResourceNotFoundException("Cet etudiant n'est pas enregistre dans la faculte");
        }
        Student theStudent = student.get();
        Optional<CoursTP> optionalCoursTP= tpRepository
                .findValidCours(code,theStudent.getGroupTimetable().getId(),todayDay,now);
        if (optionalCoursTP.isEmpty())
        {
            throw new ResourceNotFoundException("Cours Non disponible ");
        }
        CoursTP theCoursTP = optionalCoursTP.get();

        Optional<Seance> optionalSeance = seanceRepository.findByCoursTPIdAndDate(theCoursTP.getId(), today);
        if (optionalSeance.isEmpty())
        {
            throw new ResourceNotFoundException("Cours Non disponible ");
        }
        Seance theSeance = optionalSeance.get();
        boolean exists = presenceRepository.existsBySeanceIdAndStudentId(
                theSeance.getId(),
                theStudent.getId()
        );
        if (exists) {
            throw new IllegalStateException("Présence déjà enregistrée");
        }
        Presence thePresence = new Presence();
        thePresence.setStudent(theStudent);
        thePresence.setPresent(true);
        theSeance.addPresence(thePresence);

        seanceRepository.save(theSeance);

    }

    public Page<PresenceDto> getAllAbsenceByUserId(String userId , int page , int size )
    {
        Pageable pageable = PageRequest.of(page, size);
        Student student = studentRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Etudiant non trouvé pour userId : " + userId)
                );
        return  presenceRepository.findByStudentAndPresentFalse(student,pageable).map(presenceMapper::convertPresencetoDto);


    }

}
