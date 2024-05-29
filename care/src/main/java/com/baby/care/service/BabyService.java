package com.baby.care.service;

import com.baby.care.controller.repsonse.GetAllBabiesResponse;
import com.baby.care.controller.repsonse.SaveBabyResponse;
import com.baby.care.controller.request.SaveBabyRequest;
import com.baby.care.errors.AppUserNotFoundException;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        AppUser appUser = appUserService.findCurrentAppUser(token);

        if (appUser == null || appUser.getId() == null) {
            LOGGER.error("AppUser could not be found. The Baby was not saved.");
            throw new AppUserNotFoundException();
        }

        if (appUser.getParent() == null) {
            LOGGER.error("No Parent exist for this user. Baby could not be added.");
            throw new FailedToSaveBabyException();
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
                    .parent(appUser.getParent())
                    .build();

            baby = babyRepository.save(baby);

            LOGGER.info("Baby was saved.");

            Parent parent = appUser.getParent();
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
            throw new FailedToSaveBabyException();
        }
    }

    public List<GetAllBabiesResponse> getAllBabies(String token) {
        AppUser appUser = appUserService.findCurrentAppUser(token);

        if (appUser == null || appUser.getId() == null) {
            LOGGER.error("AppUser could not be found. The Baby was not saved.");
            throw new AppUserNotFoundException();
        }

        if (appUser.getParent() == null) {
            LOGGER.error("No Parent exist for this user. Baby could not be added.");
            throw new FailedToSaveBabyException();
        }

        List<GetAllBabiesResponse> responseList = new ArrayList<>();

        for(Baby baby : appUser.getParent().getBabies()) {
            GetAllBabiesResponse response = GetAllBabiesResponse.builder()
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
}
