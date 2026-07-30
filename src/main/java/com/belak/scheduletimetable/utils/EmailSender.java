package com.belak.scheduletimetable.utils;



public interface EmailSender {
    void sendEmail(String to,String subject ,  String email);
}
