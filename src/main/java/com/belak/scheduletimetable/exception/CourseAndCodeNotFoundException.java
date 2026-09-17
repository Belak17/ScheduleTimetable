package com.belak.scheduletimetable.exception;

import lombok.Getter;

public class CourseAndCodeNotFoundException extends RuntimeException {
    @Getter
    private final String code;
    @Getter
    private final String userId;
    public CourseAndCodeNotFoundException(String coursNonDisponible, String code,String userId) {
        super(coursNonDisponible);
        this.code = code;
        this.userId = userId;
    }

}
