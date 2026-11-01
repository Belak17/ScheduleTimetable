package com.belak.scheduletimetable.exception;

import com.belak.scheduletimetable.model.User;
import lombok.Getter;

import java.time.LocalDate;

public class InformationNotFoundException extends RuntimeException {
    @Getter
    private final String email;
    @Getter
    private  final User.Role role;
    public InformationNotFoundException(String message, String email, User.Role role) {
        super(message);
        this.email = email;
        this.role = role;
    }
}
