package com.baby.care.service;

import com.baby.care.controller.repsonse.SaveBabyResponse;
import com.baby.care.controller.request.SaveBabyRequest;
import com.baby.care.errors.FailedToSaveBabyException;
import com.baby.care.model.AppUser;
import com.baby.care.model.Baby;
import com.baby.care.repository.BabyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BabyService {
    private BabyRepository babyRepository;
    private AppUserService appUserService;

    public SaveBabyResponse saveBaby(SaveBabyRequest saveBabyRequest, String token) {
        AppUser appUser = appUserService.findCurrentAppUser(token);

        Baby baby = Baby.builder()
                .name(saveBabyRequest.getName())
                .dateOfBirth(saveBabyRequest.getDateOfBirth())
                .sex(saveBabyRequest.getSex())
                .weight(saveBabyRequest.getWeight())
                .height(saveBabyRequest.getHeight())
                .typeOfBirth(saveBabyRequest.getTypeOfBirth())
                .comments(saveBabyRequest.getComments())
                .parent(appUser.getParent())
                .build();

        baby = babyRepository.save(baby);

        if (baby.getId() != null) {
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
        } else {
            throw new FailedToSaveBabyException();
        }
    }
}
