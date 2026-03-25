package com.belak.scheduletimetable.record;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupInfo {
    String depname;

    String field;
    String group;
    int year;
}
