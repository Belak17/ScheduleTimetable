package com.belak.scheduletimetable.request;

import lombok.Data;

@Data
public class UpdateRequest {

    private String userId;
    private String password;

    public void setUserId(String userId) {
        this.userId = userId != null ? userId.trim() : null;
    }

    public void setPassword(String password) {
        this.password = password != null ? password.trim() : null;
    }
}