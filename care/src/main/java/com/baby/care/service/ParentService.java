package com.baby.care.service;

import com.baby.care.controller.repsonse.GetParentResponse;
import com.baby.care.controller.repsonse.SaveBabyResponse;
import com.baby.care.controller.repsonse.SaveParentResponse;
import com.baby.care.controller.request.SaveParentRequest;
import com.baby.care.controller.request.UpdateParentRequest;
import com.baby.care.errors.AppUserNotFoundException;
import com.baby.care.errors.FailedToSaveParentException;
import com.baby.care.errors.ParentNotFoundException;
import com.baby.care.model.AppUser;
import com.baby.care.model.Parent;
import com.baby.care.repository.AppUserRepository;
import com.baby.care.repository.ParentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ParentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParentService.class);

    private AppUserService appUserService;
    private ParentRepository parentRepository;
    private AppUserRepository appUserRepository;

    /**
     * Saves a new Parent object and assign it to an AppUser based on  2 criteria:
     * 1. Login token has to be valid and assigned to an AppUser
     * 2. AppUser need to not have a Parent assigned
     *
     * @param saveParentRequest information about Parent
     * @param token login token
     * @return the information that were saved about Parent
     */
    @Transactional
    public SaveParentResponse saveParent(SaveParentRequest saveParentRequest, String token) {
        AppUser appUser = appUserService.findCurrentAppUser(token);

        if (appUser == null || appUser.getId() == null) {
            LOGGER.info("AppUser could not be found. The Parent was not saved.");
            throw new AppUserNotFoundException();
        }

        if (appUser.getParent() != null) {
            LOGGER.info("Parent for this user already exists.");
            throw new FailedToSaveParentException();
        }

        try {
            Parent parent = Parent.builder()
                    .name(saveParentRequest.getName())
                    .dateOfBirth(saveParentRequest.getDateOfBirth())
                    .sex(saveParentRequest.getSex())
                    .location(saveParentRequest.getLocation())
                    .appUser(appUser)
                    .build();
            appUser.setParent(parent);

            parent = parentRepository.save(parent);
            appUserRepository.save(appUser);

            return SaveParentResponse.builder()
                    .id(parent.getId())
                    .name(parent.getName())
                    .dateOfBirth(parent.getDateOfBirth())
                    .age(parent.getAge())
                    .sex(parent.getSex())
                    .location(parent.getLocation())
                    .build();
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            throw new FailedToSaveParentException();
        }
    }

    public GetParentResponse getParent(String token) {
        AppUser appUser = isUserAndParentPresent(token);

        Optional<Parent> parent = parentRepository.findById(appUser.getParent().getId());

        if (parent.isPresent()) {
            GetParentResponse response = GetParentResponse.builder()
                    .id(parent.get().getId())
                    .name(parent.get().getName())
                    .dateOfBirth(parent.get().getDateOfBirth())
                    .sex(parent.get().getSex())
                    .location(parent.get().getLocation())
                    .build();
            response.setAge(parent.get().getAge());
            return response;
        } else {
            LOGGER.warn("Parent was not found.");
            throw new ParentNotFoundException();
        }
    }

    public SaveParentResponse updateParent(UpdateParentRequest updateParentRequest, String token) {
        AppUser appUser = isUserAndParentPresent(token);
        Parent parent = appUser.getParent();

        parent.setName(updateParentRequest.getName());
        parent.setDateOfBirth(updateParentRequest.getDateOfBirth());
        parent.setSex(updateParentRequest.getSex());
        parent.setLocation(updateParentRequest.getLocation());

        LOGGER.warn("Parent will be updated.");
        parentRepository.save(parent);

        return SaveParentResponse.builder()
                .id(parent.getId())
                .name(parent.getName())
                .dateOfBirth(parent.getDateOfBirth())
                .sex(parent.getSex())
                .age(parent.getAge())
                .location(parent.getLocation())
                .build();
    }

    private AppUser isUserAndParentPresent(String token) {
        AppUser appUser = appUserService.findCurrentAppUser(token);

        if (appUser == null || appUser.getId() == null) {
            LOGGER.debug("AppUser could not be found. The Parent was not saved.");
            throw new AppUserNotFoundException();
        }

        if (appUser.getParent() == null) {
            LOGGER.debug("Parent for this user not found.");
            throw new ParentNotFoundException();
        }

        return appUser;
    }
}
