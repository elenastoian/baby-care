package com.baby.care.service;

import com.baby.care.controller.response.GetBabyResponse;
import com.baby.care.controller.response.SaveBabyResponse;
import com.baby.care.controller.request.SaveBabyRequest;
import com.baby.care.controller.request.UpdateBabyRequest;
import com.baby.care.model.AppUser;
import com.baby.care.model.Baby;
import com.baby.care.model.Parent;
import com.baby.care.repository.BabyRepository;
import com.baby.care.repository.ParentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BabyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BabyService.class);

    private final BabyRepository babyRepository;
    private final ParentRepository parentRepository;

    @Transactional
    public SaveBabyResponse saveBaby(SaveBabyRequest saveBabyRequest) {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            Parent parent = appUser.getParent();

            Baby baby = Baby.builder()
                    .name(saveBabyRequest.getName())
                    .dateOfBirth(saveBabyRequest.getDateOfBirth())
                    .sex(saveBabyRequest.getSex())
                    .weight(saveBabyRequest.getWeight())
                    .height(saveBabyRequest.getHeight())
                    .typeOfBirth(saveBabyRequest.getTypeOfBirth())
                    .birthWeight(saveBabyRequest.getBirthWeight())
                    .comments(saveBabyRequest.getComments())
                    .parent(parent)
                    .build();

            Baby savedBaby = babyRepository.save(baby);
            LOGGER.info("Saved baby with id {}.", savedBaby.getId());

            parent.getBabies().add(savedBaby);
            parentRepository.save(parent);
            LOGGER.info("Added baby {} to parent id {}.", savedBaby.getId(), parent.getId());

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

    public List<GetBabyResponse> getAllBabies() {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<GetBabyResponse> responseList = new ArrayList<>();

        for (Baby baby : appUser.getParent().getBabies()) {
            GetBabyResponse response = mapToResponse(baby);

            responseList.add(response);
        }

        return responseList;
    }

    public GetBabyResponse getBaby(Long id) {
        Optional<Baby> baby = babyRepository.findById(id);

        if (baby.isPresent()) {
            LOGGER.info("Baby with id {} was found.", id);

            return mapToResponse(baby.get());
        }

        LOGGER.warn("Baby with id {} was not found.", id);
        return new GetBabyResponse();
    }

    @Transactional
    public GetBabyResponse updateBaby(Long id, UpdateBabyRequest request) {
        Baby existingBaby = findBabyById(id);

        updateBabyFields(existingBaby, request);
        Baby updated = this.saveBaby(existingBaby);

        return mapToResponse(updated);
    }

    protected Baby saveBaby(Baby baby) {
        return babyRepository.save(baby);
    }

    protected Optional<AppUser> getAppUserWithBaby() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            LOGGER.warn("No authenticated user found.");
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof AppUser appUser)) {
            LOGGER.warn("Principal is not an AppUser instance.");
            return Optional.empty();
        }

        if (appUser.getParent() == null || appUser.getParent().getBabies() == null) {
            LOGGER.warn("AppUser has no parent or no babies.");
            return Optional.empty();
        }

        return Optional.of(appUser);
    }

    protected Baby findBabyById(Long id) {
        return babyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Baby not found"));
    }

    private void updateBabyFields(Baby baby, UpdateBabyRequest request) {
        baby.setName(request.getName());
        baby.setDateOfBirth(request.getDateOfBirth());
        baby.setSex(request.getSex());
        baby.setWeight(request.getWeight());
        baby.setHeight(request.getHeight());
        baby.setTypeOfBirth(request.getTypeOfBirth());
        baby.setBirthWeight(request.getBirthWeight());
        baby.setComments(request.getComments());
    }

    private GetBabyResponse mapToResponse(Baby baby) {
        return GetBabyResponse.builder()
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
    }
}
