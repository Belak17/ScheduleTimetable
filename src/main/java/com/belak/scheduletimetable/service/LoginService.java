package com.belak.scheduletimetable.service;


import com.belak.scheduletimetable.exception.ResourceNotFoundException;
import com.belak.scheduletimetable.model.User;
import com.belak.scheduletimetable.repository.UserRepository;
import com.belak.scheduletimetable.request.LoginRequest;
import com.belak.scheduletimetable.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserRepository userRepository ;

//    public LoginResponse findRole(LoginRequest loginRequest)
//    {
//        User user = userRepository.findByUserId(loginRequest.getUserId())
//                .orElseThrow(() -> new ResourceNotFoundException(
//                        "Utilisateur avec l'ID " + loginRequest.getUserId() + " introuvable"
//                ));
//
//        if (user.getPassword().equals(loginRequest.getPassword()))
//        {
//             LoginResponse response = new LoginResponse() ;
//             response.setId(user.getId());
//             response.setRole(user.getRole().toString());
//             return  response ;
//        }
//        else
//        {
//            throw  new ResourceNotFoundException("Invalid password");
//        }
//    }

}
