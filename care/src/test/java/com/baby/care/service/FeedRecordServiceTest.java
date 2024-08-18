package com.baby.care.service;

import com.baby.care.controller.repsonse.FeedRecordResponse;
import com.baby.care.model.AppUser;
import com.baby.care.model.Baby;
import com.baby.care.model.FeedRecord;
import com.baby.care.model.Token;
import com.baby.care.model.enums.TypeOfFood;
import com.baby.care.repository.FeedRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedRecordServiceTest {

    @Mock
    private FeedRecordRepository feedRecordRepository;
    @Mock
    private AppUserService appUserService;
    @InjectMocks
    private FeedRecordService feedRecordService;

    private AppUser appUser = new AppUser();
    @BeforeEach
    void setUp() {
        appUser = new AppUser(1L, "user@gmail.com", "password", true, true, true, true, null, List.of(new Token()));
    }

    @Test
    void getAllFeedRecordsSuccessfully() {
        FeedRecord feedRecord = new FeedRecord(1L, LocalDateTime.of(2024, Month.AUGUST, 20, 15, 00),
                LocalDateTime.of(2024, Month.AUGUST, 20, 13, 00) , TypeOfFood.SOLID_FOOD, "N/A", new Baby());
        FeedRecord feedRecord2 = new FeedRecord(2L, LocalDateTime.of(2024, Month.AUGUST, 19, 19, 00),
                LocalDateTime.of(2024, Month.AUGUST, 19, 18, 30) , TypeOfFood.PUREE, "N/A", new Baby());

        List<FeedRecord> feedRecords = List.of(feedRecord, feedRecord2);

        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.of(appUser));
        when(feedRecordRepository.findAllByBabyIdOrderByFeedTimeDesc(anyLong())).thenReturn(feedRecords);

        List<FeedRecordResponse> result = feedRecordService.getAllFeedRecords("token", 1L);

        verify(appUserService, times(1)).findCurrentAppUser("token");
        verify(feedRecordRepository, times(1)).findAllByBabyIdOrderByFeedTimeDesc(1L);

        assertEquals(2, result.size());
    }

    @Test
    void getAllFeedRecordsUnsuccessfully_AppUserNotFound() {

        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.empty());

        List<FeedRecordResponse> result = feedRecordService.getAllFeedRecords("token", 1L);

        verify(appUserService, times(1)).findCurrentAppUser("token");
        verify(feedRecordRepository,never()).findAllByBabyIdOrderByFeedTimeDesc(1L);

        assertEquals(0, result.size());
    }

    @Test
    void getAllFeedRecordsSuccessfully_NoRecordFound() {
        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.of(appUser));
        when(feedRecordRepository.findAllByBabyIdOrderByFeedTimeDesc(anyLong())).thenReturn(Collections.emptyList());

        List<FeedRecordResponse> result = feedRecordService.getAllFeedRecords("token", 1L);

        verify(appUserService, times(1)).findCurrentAppUser("token");
        verify(feedRecordRepository, times(1)).findAllByBabyIdOrderByFeedTimeDesc(1L);

        assertEquals(0, result.size());
    }
}