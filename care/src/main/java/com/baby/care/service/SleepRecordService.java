package com.baby.care.service;

import com.baby.care.controller.response.SleepRecordResponse;
import com.baby.care.controller.request.SaveBabyCareTrackerRequest;
import com.baby.care.model.AppUser;
import com.baby.care.model.Baby;
import com.baby.care.model.SleepRecord;
import com.baby.care.repository.SleepRecordRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SleepRecordService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SleepRecordService.class);

    private final SleepRecordRepository sleepRecordRepository;
    private final AppUserService appUserService;
    private final BabyService babyService;

    public List<SleepRecordResponse> getAllSleepRecords(Long babyId) {
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
    public SleepRecordResponse saveSleepRecord(SaveBabyCareTrackerRequest saveBabyCareTrackerRequest) {
        Baby baby = babyService.findBabyById(saveBabyCareTrackerRequest.getBabyId());
        assert saveBabyCareTrackerRequest.getSleepRecord() != null;
        SleepRecord sleepRecord = SleepRecord.builder()
                .baby(baby)
                .sleepStart(saveBabyCareTrackerRequest.getSleepRecord().getSleepStart())
                .sleepEnd(saveBabyCareTrackerRequest.getSleepRecord().getSleepEnd())
                .build();

        sleepRecord = sleepRecordRepository.save(sleepRecord);

        return SleepRecordResponse.builder()
                .id(sleepRecord.getId())
                .sleepDuration(sleepRecord.getSleepDuration())
                .sleepStart(sleepRecord.getSleepStart())
                .sleepEnd(sleepRecord.getSleepEnd())
                .build();
    }
}
