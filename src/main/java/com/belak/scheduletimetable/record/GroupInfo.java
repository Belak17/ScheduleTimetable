package com.belak.scheduletimetable.record;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GroupInfo {
    String depname;

    String field;
    String group;
    int year;

}
