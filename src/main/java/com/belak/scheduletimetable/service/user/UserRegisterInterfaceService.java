package com.belak.scheduletimetable.service.user;

import com.belak.scheduletimetable.model.User;
import com.belak.scheduletimetable.request.UserRegister;

public interface UserRegisterInterfaceService {

    public boolean existsUserByUserIdAndEmail(UserRegister userRegister) ;

    public void registerUserByUserIdAndEmail(UserRegister userRegister) ;

    public  String register(UserRegister request) ;

    public  String buildEmail(String name, String link) ;

    public String signup(User appUser) ;

    public String confirmToken(String token) ;

    public void enable(String email) ;



}
