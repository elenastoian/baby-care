package com.baby.care.service;

import com.baby.care.controller.response.FeedRecordResponse;
import com.baby.care.model.AppUser;
import com.baby.care.model.FeedRecord;
import com.baby.care.repository.FeedRecordRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedRecordService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FeedRecordService.class);

    private final FeedRecordRepository feedRecordRepository;

    public List<FeedRecordResponse> getAllFeedRecords(Long babyId) {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        LOGGER.info("Authenticated user: {}", appUser.getEmail());

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
