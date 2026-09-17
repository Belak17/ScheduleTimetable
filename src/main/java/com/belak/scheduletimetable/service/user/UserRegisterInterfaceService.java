package com.belak.scheduletimetable.service.user;

import com.belak.scheduletimetable.model.User;
import com.belak.scheduletimetable.request.UserRegister;

public interface UserRegisterInterfaceService {

    public boolean existsUserByUserIdAndEmail(UserRegister userRegister) ;

    public void registerUserByUserIdAndEmail(UserRegister userRegister) ;

    public int register(UserRegister request) ;

    public  String buildEmail(String name, int token) ;

    public int signup(User appUser) ;

    public String confirmToken(int  token) ;

    public void enable(String email) ;



}
