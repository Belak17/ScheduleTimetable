package com.belak.scheduletimetable.service.courstp;

import com.belak.scheduletimetable.repository.coursTP.CoursTPRepository;
import com.belak.scheduletimetable.repository.coursTP.SeanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class CoursTPUService {
    private final CoursTPRepository tpRepository ;
    private final SeanceRepository seanceRepository;

    public void getAllPreviousGeneratedCourse()
    {
        LocalDate yesterday = LocalDate.now().minusDays(1);

    }

}
