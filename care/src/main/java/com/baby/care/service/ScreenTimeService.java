package com.baby.care.service;

import com.baby.care.controller.repsonse.ScreenTimeRecordResponse;
import com.baby.care.controller.repsonse.SleepRecordResponse;
import com.baby.care.model.AppUser;
import com.baby.care.model.ScreenTimeRecord;
import com.baby.care.repository.ScreenTimeRecordRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ScreenTimeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScreenTimeService.class);

    private final ScreenTimeRecordRepository screenTimeRecordRepository;
    private final AppUserService appUserService;
    private final BabyService babyService;

    public List<ScreenTimeRecordResponse> getAllScreenTimeRecords(String token, Long babyId) {

        Optional<AppUser> appUserOptional = appUserService.findCurrentAppUser(token);

        if (appUserOptional.isEmpty()) {
            LOGGER.info("ScreenTimeService - AppUser not found.");
            return Collections.emptyList();
        }

        List<ScreenTimeRecord> screenTimeRecords = screenTimeRecordRepository.findAllByBabyIdOrderByScreenTimeStartDesc(babyId);

        return screenTimeRecords.stream()
                .map(record -> ScreenTimeRecordResponse.builder()
                        .id(record.getId())
                        .screenTimeStart(record.getScreenTimeStart())
                        .screenTimeEnd(record.getScreenTimeEnd())
                        .screenTimeDuration(record.getScreenTimeDuration())
                        .comments(record.getComments())
                        .build())
                .collect(Collectors.toList());
    }
}
