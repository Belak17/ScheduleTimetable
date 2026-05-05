package com.belak.scheduletimetable.component;

import com.belak.scheduletimetable.dto.PresenceDto;
import com.belak.scheduletimetable.model.Presence;
import org.springframework.stereotype.Component;

@Component
public class PresenceMapper {
    public PresenceDto convertPresencetoDto(Presence presence)
    {
        PresenceDto presenceDto = new PresenceDto();
        presenceDto.setDate(presence.getSeance().getDate());
        presenceDto.setIntitule(presence.getSeance().getCoursTP().getIntitule());
        return presenceDto ;
    }
}
