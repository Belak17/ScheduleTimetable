package com.belak.scheduletimetable.exception;

import lombok.Getter;
import org.jspecify.annotations.Nullable;


public class StudentNotFoundException extends   RuntimeException{
    @Getter
    private String userId;
    @Getter
    private String code;
    public StudentNotFoundException(String message,String userId,String code) {
        super(message);
        this.userId = userId ;
        this.code = code ;
    }
}
