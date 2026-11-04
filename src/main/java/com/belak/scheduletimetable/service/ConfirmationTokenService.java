package com.belak.scheduletimetable.service;

import com.belak.scheduletimetable.model.ConfirmationToken;
import com.belak.scheduletimetable.model.User;
import com.belak.scheduletimetable.repository.ConfirmationTokenRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    private  final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken confirmationToken)
    {
        confirmationTokenRepository.save(confirmationToken);
    }


    public Optional<ConfirmationToken> getToken(int token) {
        return confirmationTokenRepository.findByToken(token);
    }

    @Transactional
    public void deletePreviousTokens(User user) {
        confirmationTokenRepository.deleteByAppUser(user);
    }
}