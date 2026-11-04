package com.belak.scheduletimetable.exception;

import com.belak.scheduletimetable.model.User;

public class AccountAlreadyActivated extends RuntimeException {
    private User.Role role ;
    public AccountAlreadyActivated(String message, User.Role role) {
        super(message);
        this.role = role;
    }
}
