package com.belak.scheduletimetable.exception;

import lombok.Getter;

public class CourseAndCodeNotFoundException extends RuntimeException {
    @Getter
    private final String code;
    public CourseAndCodeNotFoundException(String coursNonDisponible, String code) {
        super(coursNonDisponible);
        this.code = code;
    }

}
