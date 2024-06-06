package com.baby.care.service;

import com.baby.care.controller.repsonse.SaveUserResponse;
import com.baby.care.controller.request.UpdateUserRequest;
import com.baby.care.model.AppUser;
import com.baby.care.repository.AppUserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AppUserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppUserService.class);
    private AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Finds the AppUser that has the given token assigned
     *
     * @param token is the token related to the AppUser
     * @return the AppUser that was found or a new empty AppUser if no user was found
     */
    @Transactional
    public Optional<AppUser> findCurrentAppUser(String token) {
        try {
            token = token.substring(7);
            Optional<AppUser> appUserOptional = appUserRepository.findByTokensToken(token);

            if (appUserOptional.isPresent()) {
                return appUserOptional;
            }

            LOGGER.info("AppUser has not been found by the authentication token.");
            return Optional.empty();

        } catch(Exception e) {
            LOGGER.error(e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Update user info
     *
     * @param updateUserRequest contains email and password
     * @param token
     * @return
     */
    @Transactional
    public SaveUserResponse updateAppUser(UpdateUserRequest updateUserRequest, String token) {
        Optional<AppUser> appUser = this.findCurrentAppUser(token);

        if (appUser.isEmpty()) {
           return new SaveUserResponse();
        }

        if (updateUserRequest.getEmail() != null) {
            appUser.get().setEmail(updateUserRequest.getEmail());
        }

        if (updateUserRequest.getPassword() != null) {
            appUser.get().setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
        }

        appUserRepository.save(appUser.get());

        return new SaveUserResponse(appUser.get().getEmail());
    }
}