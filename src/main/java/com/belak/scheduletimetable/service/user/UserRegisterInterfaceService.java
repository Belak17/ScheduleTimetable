package com.belak.scheduletimetable.service.user;

import com.belak.scheduletimetable.request.UserRegister;

public interface UserRegisterInterfaceService {

    public boolean existsUserByUserIdAndEmail(UserRegister userRegister) ;

    public void registerUserByUserIdAndEmail(UserRegister userRegister) ;
}
