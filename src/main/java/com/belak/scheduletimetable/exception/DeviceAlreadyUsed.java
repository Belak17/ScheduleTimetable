package com.belak.scheduletimetable.exception;

import lombok.Getter;

public class DeviceAlreadyUsed extends RuntimeException {
    @Getter
    private String userId ;
    public DeviceAlreadyUsed(String message,String userId) {
        super(message);
        this.userId = userId;
    }
}
