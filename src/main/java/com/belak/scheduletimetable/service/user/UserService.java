package com.belak.scheduletimetable.service.user;

import com.belak.scheduletimetable.dto.UserDto;
import com.belak.scheduletimetable.exception.ResourceNotFoundException;
import com.belak.scheduletimetable.model.ConfirmationToken;
import com.belak.scheduletimetable.model.User;
import com.belak.scheduletimetable.repository.UserRepository;
import com.belak.scheduletimetable.response.LoginResponse;
import com.belak.scheduletimetable.service.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private  final UserRepository userRepository ;
    private final  static  String USER_NOT_FOUND_MSG =
            "user with email %s not found";

    private  final ConfirmationTokenService confirmationTokenService;
    public LoginResponse getUserData(String userId)
    {
        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new ResourceNotFoundException("User Not Found"));
        LoginResponse response = new LoginResponse() ;
        response.setNom(user.getNom());
        response.setPrenom(user.getPrenom());
        return response ;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole())
        );

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                authorities
        );
    }





}
