package com.baby.care.service;

import com.baby.care.controller.repsonse.GetBabyResponse;
import com.baby.care.controller.repsonse.SaveBabyResponse;
import com.baby.care.controller.request.SaveBabyRequest;
import com.baby.care.controller.request.UpdateBabyRequest;
import com.baby.care.errors.AppUserNotFoundException;
import com.baby.care.errors.BabyNotFoundException;
import com.baby.care.errors.FailedToSaveBabyException;
import com.baby.care.model.AppUser;
import com.baby.care.model.Baby;
import com.baby.care.model.Parent;
import com.baby.care.repository.BabyRepository;
import com.baby.care.repository.ParentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class BabyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BabyService.class);

    private BabyRepository babyRepository;
    private AppUserService appUserService;
    private ParentRepository parentRepository;

    /**
     * Save a new Baby and assign it to its Parent
     *
     * @param saveBabyRequest contains information about Baby
     * @param token is used to find the current AppUser and Parent
     * @return information that were saved in the database
     */
    @Transactional
    public SaveBabyResponse saveBaby(SaveBabyRequest saveBabyRequest, String token) {
        Optional<AppUser> appUser = isUserAndBabyPresent(token);

        if (appUser.isEmpty()) {
            LOGGER.warn("User with baby was not found.");
            return new SaveBabyResponse();
        }

        try {
            Baby baby = Baby.builder()
                    .name(saveBabyRequest.getName())
                    .dateOfBirth(saveBabyRequest.getDateOfBirth())
                    .sex(saveBabyRequest.getSex())
                    .weight(saveBabyRequest.getWeight())
                    .height(saveBabyRequest.getHeight())
                    .typeOfBirth(saveBabyRequest.getTypeOfBirth())
                    .birthWeight(saveBabyRequest.getBirthWeight())
                    .comments(saveBabyRequest.getComments())
                    .parent(appUser.get().getParent())
                    .build();

            baby = babyRepository.save(baby);

            LOGGER.info("Baby was saved.");

            Parent parent = appUser.get().getParent();
            parent.getBabies().add(baby);
            parentRepository.save(parent);

            LOGGER.info("Baby was added to Parent and saved.");

            return SaveBabyResponse.builder()
                    .id(baby.getId())
                    .name(baby.getName())
                    .dateOfBirth(baby.getDateOfBirth())
                    .age(baby.getAge())
                    .sex(baby.getSex())
                    .weight(baby.getWeight())
                    .height(baby.getHeight())
                    .typeOfBirth(baby.getTypeOfBirth())
                    .birthWeight(baby.getBirthWeight())
                    .comments(baby.getComments())
                    .build();

        } catch (Exception e) {
            LOGGER.error(Arrays.toString(e.getStackTrace()));
            LOGGER.warn("Error occurred while saving the Baby.");
            return new SaveBabyResponse();
        }
    }

    public List<GetBabyResponse> getAllBabies(String token) {
        Optional<AppUser> appUser = isUserAndBabyPresent(token);

        if (appUser.isEmpty()) {
            LOGGER.warn("User with baby was not found.");
            return Collections.emptyList();
        }

        List<GetBabyResponse> responseList = new ArrayList<>();

        for(Baby baby : appUser.get().getParent().getBabies()) {
            GetBabyResponse response = GetBabyResponse.builder()
                    .id(baby.getId())
                    .name(baby.getName())
                    .dateOfBirth(baby.getDateOfBirth())
                    .age(baby.getAge())
                    .sex(baby.getSex())
                    .weight(baby.getWeight())
                    .height(baby.getHeight())
                    .typeOfBirth(baby.getTypeOfBirth())
                    .birthWeight(baby.getBirthWeight())
                    .comments(baby.getComments())
                    .build();

            responseList.add(response);
        }

        return responseList;
    }

    public GetBabyResponse getBaby(Long id, String token) {
        Optional<AppUser> appUser = isUserAndBabyPresent(token);

        if (appUser.isEmpty()) {
            LOGGER.warn("User with baby was not found.");
            return new GetBabyResponse();
        }

        Optional<Baby> baby = babyRepository.findById(id);

        if (baby.isPresent()) {
            LOGGER.info("Baby with id {} was found.", id);

            return GetBabyResponse.builder()
                    .id(baby.get().getId())
                    .name(baby.get().getName())
                    .dateOfBirth(baby.get().getDateOfBirth())
                    .age(baby.get().getAge())
                    .sex(baby.get().getSex())
                    .weight(baby.get().getWeight())
                    .height(baby.get().getHeight())
                    .typeOfBirth(baby.get().getTypeOfBirth())
                    .birthWeight(baby.get().getBirthWeight())
                    .comments(baby.get().getComments())
                    .build();
        }

        LOGGER.warn("Baby with id {} was not found.", id);
        return new GetBabyResponse();
    }

    public GetBabyResponse updateBaby(UpdateBabyRequest updateBabyRequest, String token) {
        Optional<AppUser> appUser = isUserAndBabyPresent(token);

        if (appUser.isEmpty()) {
            LOGGER.warn("User with baby was not found.");
            return new GetBabyResponse();
        }

        Baby updateBaby = Baby.builder()
                .name(updateBabyRequest.getName())
                .dateOfBirth(updateBabyRequest.getDateOfBirth())
                .sex(updateBabyRequest.getSex())
                .weight(updateBabyRequest.getWeight())
                .height(updateBabyRequest.getHeight())
                .typeOfBirth(updateBabyRequest.getTypeOfBirth())
                .birthWeight(updateBabyRequest.getBirthWeight())
                .comments(updateBabyRequest.getComments())
                .parent(appUser.get().getParent())
                .build();

        updateBaby = babyRepository.save(updateBaby);

        return GetBabyResponse.builder()
                .id(updateBaby.getId())
                .name(updateBaby.getName())
                .dateOfBirth(updateBaby.getDateOfBirth())
                .age(updateBaby.getAge())
                .sex(updateBaby.getSex())
                .weight(updateBaby.getWeight())
                .height(updateBaby.getHeight())
                .typeOfBirth(updateBaby.getTypeOfBirth())
                .birthWeight(updateBaby.getBirthWeight())
                .comments(updateBaby.getComments())
                .build();

    }

    private Optional<AppUser> isUserAndBabyPresent(String token) {
        Optional<AppUser> appUser = appUserService.findCurrentAppUser(token);

        if (appUser.isEmpty() || appUser.get().getParent() == null || appUser.get().getParent().getBabies() == null) {
            LOGGER.warn("AppUser with existing babies could not be found.");
            return Optional.empty();
        } else {
            return appUser;
        }
    }
}
