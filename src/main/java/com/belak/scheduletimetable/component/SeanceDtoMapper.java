package com.belak.scheduletimetable.component;

import com.belak.scheduletimetable.dto.coursTP.SeanceDto;
import com.belak.scheduletimetable.model.Seance;
import org.springframework.stereotype.Component;

@Component
public class SeanceDtoMapper {

    public SeanceDto toDto(Seance seance) {

        SeanceDto dto = new SeanceDto();

        dto.setDate(seance.getDate());

        dto.setAbsencesProcessed(seance.isAbsencesProcessed());

        if (seance.getCoursTP() != null) {

            dto.setIntitule(
                    seance.getCoursTP().getIntitule() != null
                            ? seance.getCoursTP().getIntitule()
                            : "NA"
            );

            dto.setDebut(seance.getCoursTP().getDebut());

            dto.setFin(seance.getCoursTP().getFin());

            if (seance.getCoursTP().getSalle() != null) {
                dto.setSalle(
                        seance.getCoursTP().getSalle().getCode() != null
                                ? seance.getCoursTP().getSalle().getCode()
                                : "NA"
                );
            } else {
                dto.setSalle("NA");
            }

        } else {
            dto.setIntitule("NA");
            dto.setSalle("NA");
        }

        return dto;
    }
}
