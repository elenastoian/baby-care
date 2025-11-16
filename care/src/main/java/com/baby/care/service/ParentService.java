package com.baby.care.service;

import com.baby.care.controller.response.GetParentResponse;
import com.baby.care.controller.response.SaveParentResponse;
import com.baby.care.controller.request.SaveParentRequest;
import com.baby.care.controller.request.UpdateParentRequest;
import com.baby.care.errors.ParentNotFoundException;
import com.baby.care.model.AppUser;
import com.baby.care.model.Parent;
import com.baby.care.repository.AppUserRepository;
import com.baby.care.repository.ParentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParentService.class);

    private final AppUserService appUserService;
    private final ParentRepository parentRepository;
    private final AppUserRepository appUserRepository;

    /**
     * Save a new Parent for the currently authenticated AppUser.
     * If the AppUser already has a Parent, return the existing Parent.
     */
    @Transactional
    public SaveParentResponse saveParent(SaveParentRequest saveParentRequest) {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (appUser.getParent() != null) {
            LOGGER.warn("Parent for this user already exists.");
            return SaveParentResponse.builder()
                    .id(appUser.getParent().getId())
                    .name(appUser.getParent().getName())
                    .dateOfBirth(appUser.getParent().getDateOfBirth())
                    .age(appUser.getParent().getAge())
                    .sex(appUser.getParent().getSex())
                    .location(appUser.getParent().getLocation())
                    .build();
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
            LOGGER.error(e.getMessage());
            LOGGER.warn("Error while saving parent.");
            return new SaveParentResponse();
        }
    }

    public GetParentResponse getParent() {
        try {
            AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Optional<Parent> parent = parentRepository.findById(appUser.getParent().getId());

            //CHECK PARENT
            if (parent.isEmpty()) {
                LOGGER.info("Parent not found for user with id {}.", appUser.getId());
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
            LOGGER.error("Parent not found for this user.");
            throw new ParentNotFoundException();
        }
    }

    @Transactional
    public SaveParentResponse updateParent(UpdateParentRequest updateParentRequest) {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (appUser.getParent() != null) {
            Parent parent = appUser.getParent();

            parent.setName(updateParentRequest.getName());
            parent.setDateOfBirth(updateParentRequest.getDateOfBirth());
            parent.setSex(updateParentRequest.getSex());
            parent.setLocation(updateParentRequest.getLocation());

            LOGGER.info("Parent will be updated.");
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
}
