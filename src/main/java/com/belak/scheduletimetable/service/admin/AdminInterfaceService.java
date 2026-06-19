package com.belak.scheduletimetable.service.admin;

import com.belak.scheduletimetable.dto.AdminProfileDto;

public interface AdminInterfaceService {

    public AdminProfileDto findByUserId(String userId) ;


    public void updatePassword(String oldPassword,
                               String newPassword,
                               String confPassword,
                               String userId) ;

    public void updateInfo(String userId, String address, String telephone, String code) ;

    public void updateEmail(String nouvEmail, String confEmail, String userId) ;
}
