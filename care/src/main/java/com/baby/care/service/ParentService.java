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

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ParentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParentService.class);

    private final AppUserService appUserService;
    private final ParentRepository parentRepository;
    private final AppUserRepository appUserRepository;

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
        Optional<AppUser> appUser = appUserService.findCurrentAppUser(token);

        if (appUser.isEmpty()) {
            LOGGER.warn("AppUser could not be found. The Parent was not saved.");
            return new SaveParentResponse();
        }

        if (appUser.get().getParent() != null) {
            LOGGER.warn("Parent for this user already exists.");
            return SaveParentResponse.builder()
                    .id(appUser.get().getParent().getId())
                    .name(appUser.get().getParent().getName())
                    .dateOfBirth(appUser.get().getParent().getDateOfBirth())
                    .age(appUser.get().getParent().getAge())
                    .sex(appUser.get().getParent().getSex())
                    .location(appUser.get().getParent().getLocation())
                    .build();
        }

        try {
            Parent parent = Parent.builder()
                    .name(saveParentRequest.getName())
                    .dateOfBirth(saveParentRequest.getDateOfBirth())
                    .sex(saveParentRequest.getSex())
                    .location(saveParentRequest.getLocation())
                    .appUser(appUser.get())
                    .build();
            appUser.get().setParent(parent);

            parent = parentRepository.save(parent);
            appUserRepository.save(appUser.get());

            return SaveParentResponse.builder()
                    .id(parent.getId())
                    .name(parent.getName())
                    .dateOfBirth(parent.getDateOfBirth())
                    .age(parent.getAge())
                    .sex(parent.getSex())
                    .location(parent.getLocation())
                    .build();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.warn("Error while saving parent.");
            return new SaveParentResponse();
        }
    }

    public GetParentResponse getParent(String token) {
        try {
            Optional<AppUser> appUser = isUserAndParentPresent(token);

            //CHECK USER
            if (appUser.isEmpty()) {
                LOGGER.info("User not found.");
                return new GetParentResponse();
            }

            Optional<Parent> parent = parentRepository.findById(appUser.get().getParent().getId());

            //CHECK PARENT
            if (parent.isEmpty()) {
                LOGGER.info("Parent not found for user with id {}.", appUser.get().getId());
                return new GetParentResponse();
            }

            GetParentResponse response = GetParentResponse.builder()
                    .id(parent.get().getId())
                    .name(parent.get().getName())
                    .dateOfBirth(parent.get().getDateOfBirth())
                    .sex(parent.get().getSex())
                    .location(parent.get().getLocation())
                    .build();
            response.setAge(parent.get().getAge());
            return response;
        } catch(Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.error(Arrays.toString(e.getStackTrace()));
            throw new ParentNotFoundException();
        }
    }

    @Transactional
    public SaveParentResponse updateParent(UpdateParentRequest updateParentRequest, String token) {
        Optional<AppUser> appUser = isUserAndParentPresent(token);

        if (appUser.isPresent()) {
            Parent parent = appUser.get().getParent();

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
        } else {
            return new SaveParentResponse();
        }
    }

    private Optional<AppUser> isUserAndParentPresent(String token) {
        Optional<AppUser> optionalAppUser = appUserService.findCurrentAppUser(token);

        if(optionalAppUser.isPresent()){
            if(optionalAppUser.get().getParent() != null){
                return optionalAppUser;
            }
        }

        LOGGER.warn("ParentService - Parent was not found.");
        return Optional.empty();
    }
}
