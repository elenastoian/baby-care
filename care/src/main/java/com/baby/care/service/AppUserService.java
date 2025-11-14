package com.baby.care.service;

import com.baby.care.controller.response.SaveUserResponse;
import com.baby.care.controller.request.UpdateUserRequest;
import com.baby.care.model.AppUser;
import com.baby.care.repository.AppUserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppUserService.class);
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found."));
    }

    /**
     * Is deprecated, because the authentication is now handled by Spring Security context.
     * Use: AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
     */
    @Deprecated
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

    @Transactional
    public SaveUserResponse updateAppUser(UpdateUserRequest updateUserRequest) {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (appUser.getId() == null) {
           return new SaveUserResponse();
        }

        Optional.ofNullable(updateUserRequest.getEmail()).ifPresent(appUser::setEmail);
        Optional.ofNullable(updateUserRequest.getPassword())
                .map(passwordEncoder::encode)
                .ifPresent(appUser::setPassword);

        appUserRepository.save(appUser);
        return new SaveUserResponse(appUser.getEmail());
    }
}