package com.belak.scheduletimetable.exception;

import com.belak.scheduletimetable.model.User;
import lombok.Getter;

public class AccountAlreadyActivated extends RuntimeException {
    @Getter
    private User.Role role ;
    public AccountAlreadyActivated(String message, User.Role role) {
        super(message);
        this.role = role;
    }
}
