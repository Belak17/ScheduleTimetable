package com.belak.scheduletimetable.service.presence;

import com.belak.scheduletimetable.model.Presence;
import com.belak.scheduletimetable.model.Seance;
import com.belak.scheduletimetable.model.Student;
import com.belak.scheduletimetable.repository.PresenceRepository;
import com.belak.scheduletimetable.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AbsenceService {
    private  final PresenceRepository presenceRepository ;
    private  final StudentRepository studentRepository ;
    public void generateAbsences(Seance seance) {

         /*List<Student> students = seance.getCoursTP()
                .getGroupTimetable()
                .getStudents();*/
        List<Student> students = studentRepository.findByGroupId(seance.getCoursTP().getGroupTimetable().getId());

        for (Student student : students) {

            boolean isPresent = presenceRepository
                    .existsByStudentIdAndSeanceIdAndPresentTrue(
                            student.getId(),
                            seance.getId()
                    );
            if (!isPresent) {
                createAbsence(student, seance);
            }
        }
    }
    public void createAbsence(Student student, Seance seance) {

        Presence absence = new Presence();

        absence.setStudent(student);
        absence.setSeance(seance);
        absence.setPresent(false);
        absence.setLocalTime(LocalTime.now(ZoneId.of("Africa/Tunis")));
        presenceRepository.save(absence);
    }
}
