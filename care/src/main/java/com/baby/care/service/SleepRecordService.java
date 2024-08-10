package com.baby.care.service;

import com.baby.care.controller.repsonse.SleepRecordResponse;
import com.baby.care.controller.request.SaveBabyCareTrackerRequest;
import com.baby.care.model.AppUser;
import com.baby.care.model.Baby;
import com.baby.care.model.SleepRecord;
import com.baby.care.repository.SleepRecordRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SleepRecordService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SleepRecordService.class);

    private final SleepRecordRepository sleepRecordRepository;
    private final AppUserService appUserService;
    private final BabyService babyService;

    public List<SleepRecordResponse> getAllSleepRecords(String token, Long babyId) {
        Optional<AppUser> appUserOptional = appUserService.findCurrentAppUser(token);

        if (appUserOptional.isEmpty()) {
            LOGGER.info("SleepRecordService - AppUser not found.");
            return Collections.emptyList();
        }

        List<SleepRecord> sleepRecords = sleepRecordRepository.findAllByBabyIdOrderBySleepStartDesc(babyId);

        return sleepRecords.stream()
                .map(record -> SleepRecordResponse.builder()
                        .id(record.getId())
                        .sleepStart(record.getSleepStart())
                        .sleepEnd(record.getSleepEnd())
                        .sleepDuration(record.getSleepDuration())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public SleepRecordResponse saveSleepRecord(String token, SaveBabyCareTrackerRequest saveBabyCareTrackerRequest) {
        // Find the current user
        Optional<AppUser> optionalAppUser = appUserService.findCurrentAppUser(token);
        if (optionalAppUser.isEmpty()) {
            LOGGER.info("SleepRecordService - User not found for token: {}", token);
            return new SleepRecordResponse(); // Return an empty response
        }

        // Find the baby associated with the request
        Optional<Baby> optionalBaby = babyService.findBabyById(saveBabyCareTrackerRequest.getBabyId());
        if (optionalBaby.isEmpty()) {
            LOGGER.info("SleepRecordService - Baby not found with id: {}", saveBabyCareTrackerRequest.getBabyId());
            return new SleepRecordResponse(); // Return an empty response
        }

        Baby baby = optionalBaby.get();


        // Build and save the SleepRecord
        SleepRecord sleepRecord = SleepRecord.builder()
                .sleepStart(saveBabyCareTrackerRequest.getSleepRecord().getSleepStart())
                .sleepEnd(saveBabyCareTrackerRequest.getSleepRecord().getSleepEnd())
                .build();

        sleepRecord = sleepRecordRepository.save(sleepRecord);

        // Return a response object with the saved SleepRecord details
        return SleepRecordResponse.builder()
                .id(sleepRecord.getId())
                .sleepDuration(sleepRecord.getSleepDuration())
                .sleepStart(sleepRecord.getSleepStart())
                .sleepEnd(sleepRecord.getSleepEnd())
                .build();
    }
}
