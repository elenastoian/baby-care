package com.baby.care.service;

import com.baby.care.controller.repsonse.FeedRecordResponse;
import com.baby.care.model.AppUser;
import com.baby.care.model.FeedRecord;
import com.baby.care.repository.FeedRecordRepository;
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
public class FeedRecordService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FeedRecordService.class);

    private FeedRecordRepository feedRecordRepository;
    private final AppUserService appUserService;

    public List<FeedRecordResponse> getAllFeedRecords(String token, Long babyId) {
        Optional<AppUser> appUserOptional = appUserService.findCurrentAppUser(token);

        if (appUserOptional.isEmpty()) {
            LOGGER.info("FeedRecordService - AppUser not found.");
            return Collections.emptyList();
        }

        List<FeedRecord> feedRecords = feedRecordRepository.findAllByBabyIdOrderByFeedTimeDesc(babyId);

        return feedRecords.stream()
                .map(record -> FeedRecordResponse.builder()
                        .id(record.getId())
                        .feedTime(record.getFeedTime())
                        .typeOfFood(record.getTypeOfFood())
                        .comments(record.getComments())
                        .build())
                .collect(Collectors.toList());
    }
}
