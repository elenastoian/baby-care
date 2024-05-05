package com.baby.care.service.security;

import com.baby.care.model.AppUser;
import com.baby.care.model.Token;
import com.baby.care.repository.TokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;


    public Optional<Token> findByTokenAndUser(AppUser appUser, String token) {

        return tokenRepository.findByTokenAndUser(token, appUser);
    }
}
