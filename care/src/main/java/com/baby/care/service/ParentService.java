package com.baby.care.service;

import com.baby.care.controller.repsonse.SaveParentResponse;
import com.baby.care.controller.request.SaveParentRequest;
import com.baby.care.errors.AppUserNotFoundException;
import com.baby.care.errors.FailedToSaveParentException;
import com.baby.care.model.AppUser;
import com.baby.care.model.Baby;
import com.baby.care.model.Parent;
import com.baby.care.repository.AppUserRepository;
import com.baby.care.repository.ParentRepository;
import com.baby.care.service.security.EmailService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ParentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParentService.class);


    private AppUserService appUserService;
    private ParentRepository parentRepository;
    private AppUserRepository appUserRepository;

    @Transactional
    public SaveParentResponse saveParent(SaveParentRequest saveParentRequest, String token) {
        AppUser appUser = appUserService.findCurrentAppUser(token);

        //Find User
        if (appUser == null || appUser.getId() == null) {
            LOGGER.info("AppUser could not be found. The Parent was not saved.");
            throw new AppUserNotFoundException();
        }

        //Check User to not have an assigned Parent
        if (appUser.getParent() != null) {
            LOGGER.info("Parent for this user already exists.");
            throw new FailedToSaveParentException();
        }

        try {
            Parent parent = Parent.builder()
                    .name(saveParentRequest.getName())
                    .age(saveParentRequest.getAge())
                    .sex(saveParentRequest.getSex())
                    .location(saveParentRequest.getLocation())
                    .appUser(appUser)
                    .build();
            appUser.setParent(parent);

            parent = parentRepository.save(parent);
            appUser = appUserRepository.save(appUser);

            return SaveParentResponse.builder()
                    .id(parent.getId())
                    .name(parent.getName())
                    .age(parent.getAge())
                    .sex(parent.getSex())
                    .location(parent.getLocation())
                    .build();
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            throw new FailedToSaveParentException();
        }
    }
}
