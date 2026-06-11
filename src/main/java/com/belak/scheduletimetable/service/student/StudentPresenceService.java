package com.belak.scheduletimetable.service.student;

import com.belak.scheduletimetable.dto.AbsenceOverviewDto;
import com.belak.scheduletimetable.dto.OverviewCoursTPDto;
import com.belak.scheduletimetable.exception.ResourceNotFoundException;
import com.belak.scheduletimetable.model.CoursTP;
import com.belak.scheduletimetable.model.Seance;
import com.belak.scheduletimetable.model.Student;
import com.belak.scheduletimetable.repository.CoursTPRepository;
import com.belak.scheduletimetable.repository.PresenceRepository;
import com.belak.scheduletimetable.repository.SeanceRepository;
import com.belak.scheduletimetable.repository.StudentRepository;
import com.belak.scheduletimetable.service.presence.PresenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class StudentPresenceService {
    private  final StudentRepository studentRepository ;
    private  final PresenceRepository presenceRepository ;
    private final SeanceRepository seanceRepository ;
    private  final CoursTPRepository tpRepository ;
    public List<CoursTP> getCoursTPByUserId(String userId)
    {
        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() ->new ResourceNotFoundException("Etudiant Non trouvé"));
        if (student.getGroupTimetable()!=null)
        {
            return student.getGroupTimetable().getCoursTPList();
        }
        return  List.of();
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

    public List<OverviewCoursTPDto> getAllStudentOverviewByUserId(String userId)
    {
        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() ->new ResourceNotFoundException("Etudiant Non trouvé"));
        return getCoursTPByUserId(student.getUserId())
                .stream()
                .map(coursTP -> getOverviewByUserIdAndCoursTP(coursTP,student))
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
        return presenceRepository.findAbsenceOverview(student.getId(),coursTPId);
    }

}
