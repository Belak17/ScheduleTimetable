package com.belak.scheduletimetable.service.student;

import com.belak.scheduletimetable.dto.presence.AbsenceOverviewDto;
import com.belak.scheduletimetable.dto.coursTP.OverviewCoursTPDto;
import com.belak.scheduletimetable.enumeration.Semester;
import com.belak.scheduletimetable.exception.ResourceNotFoundException;
import com.belak.scheduletimetable.model.CoursTP;
import com.belak.scheduletimetable.model.Seance;
import com.belak.scheduletimetable.model.Student;
import com.belak.scheduletimetable.repository.coursTP.CoursTPRepository;
import com.belak.scheduletimetable.repository.presence.PresenceRepository;
import com.belak.scheduletimetable.repository.coursTP.SeanceRepository;
import com.belak.scheduletimetable.repository.student.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class StudentPresenceService implements  StudentPresenceInterfaceService {
    private  final StudentRepository studentRepository ;
    private  final PresenceRepository presenceRepository ;
    private final SeanceRepository seanceRepository ;
    private  final CoursTPRepository tpRepository ;
    public List<CoursTP> getCoursTPByUserId(String userId)
    {
        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() ->new ResourceNotFoundException("Etudiant Non trouvé"));
        log.info("getCoursTPByUserId student {}",student);
        return  student.getTimetables().stream()
                .filter(t -> t.getSemester() == Semester.fromDate(LocalDate.now(ZoneId.of("Africa/Tunis"))))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No  timetable found for this semester ")).getCoursTPList();

        //return student.getGroupTimetable().getCoursTPList();
    }
    public boolean isPresent(Student student, Seance seance) {
        return presenceRepository.existsByStudentIdAndSeanceIdAndPresentTrue(student.getId(), seance.getId());
    }
    public int getTotalSeancesByCoursTP(CoursTP coursTP)
    {
        return coursTP.getSeances().size();
    }
    public int getTotalAbsencesByCoursTP(CoursTP coursTP, Student student)
    {
        return (int) coursTP.getSeances().stream()
                .filter(seance ->
                        !isPresent(
                                student,
                                seance
                        )
                )
                .count();
    }
    public List<OverviewCoursTPDto> getAllStudentOverviewByStudent(Student student)
    {
        return getCoursTPByUserId(student.getUserId())
                .stream()
                .map(coursTP -> getOverviewByUserIdAndCoursTP(coursTP,student))
                .toList();
    }
    public List<OverviewCoursTPDto> getAllStudentOverviewByUserId(String userId , Semester semester)
    {
        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() ->new ResourceNotFoundException("Etudiant Non trouvé"));
        log.info("getAllStudentOverviewByUserId student {}",student);
        return getCoursTPByUserId(student.getUserId())
            .stream()
            .filter(c -> c.getGroupTimetable().getSemester() == semester)
            .map(c -> getOverviewByUserIdAndCoursTP(c, student))
            .toList();
    }
    public OverviewCoursTPDto getOverviewByUserIdAndCoursTP(CoursTP coursTP , Student student)
    {
        return new OverviewCoursTPDto(coursTP.getId(), coursTP.getIntitule(),getTotalAbsencesByCoursTP(coursTP,student)
                ,getTotalSeancesByCoursTP(coursTP));
    }
    public List<AbsenceOverviewDto> getAllAbsenceByUserIdAndCoursTP(String userId , Long coursTPId)
    {

        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() ->new ResourceNotFoundException("Etudiant Non trouvé"));
        log.info("getAllAbsenceByUserIdAndCoursTP student {}",student);
        return presenceRepository.findAbsenceOverview(student.getId(),coursTPId);
    }
}
