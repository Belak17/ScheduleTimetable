package com.belak.scheduletimetable.exception;

public class AccountNotActivatedException extends RuntimeException {
    String userId ;
    String code ;
    public AccountNotActivatedException(String message,String code,String userId)
    {
        super(message);
        this.userId = userId ;
        this.code = code ;

    }
}
