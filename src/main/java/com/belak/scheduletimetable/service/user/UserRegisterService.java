package com.belak.scheduletimetable.service.user;

import com.belak.scheduletimetable.model.User;
import com.belak.scheduletimetable.repository.UserRepository;
import com.belak.scheduletimetable.request.UserRegister;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegisterService implements UserRegisterInterfaceService {
    private  final UserRepository userRepository ;
    private  final PasswordEncoder passwordEncoder ;

    public boolean existsUserByUserIdAndEmail(UserRegister userRegister)
    {
        return userRepository.existsByUserIdAndEmail(userRegister.getUserId(),userRegister.getEmail());
    }

    public void registerUserByUserIdAndEmail(UserRegister userRegister)
    {
        if (existsUserByUserIdAndEmail(userRegister))
        {
            User user = userRepository.findByEmail(userRegister.getEmail()).get();
            user.setEnabled(true) ;
            user.setPassword(passwordEncoder.encode(userRegister.getPassword()));
            userRepository.save(user);
        }
    }
}
