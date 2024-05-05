package com.baby.care.service;

import com.baby.care.model.AppUser;
import com.baby.care.repository.AppUserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AppUserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppUserService.class);
    private AppUserRepository appUserRepository;

    /**
     * Finds the AppUser that has the given token assigned
     *
     * @param token is the token related to the AppUser
     * @return the AppUser that was found or a new empty AppUser if no user was found
     */
    @Transactional
    public AppUser findCurrentAppUser(String token) {
        try {
            token = token.substring(7);
            Optional<AppUser> appUserOptional = appUserRepository.findByTokensToken(token);

            if (appUserOptional.isPresent()) {
                return appUserOptional.get();
            }

            LOGGER.info("AppUser has not been found by the authentication token.");
            return new AppUser();

        } catch(Exception e) {
            LOGGER.error(e.getMessage());
            return new AppUser();
        }
    }
}