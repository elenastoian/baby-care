package com.baby.care.service;

import com.baby.care.controller.repsonse.ScreenTimeRecordResponse;
import com.baby.care.model.AppUser;
import com.baby.care.model.Baby;
import com.baby.care.model.ScreenTimeRecord;
import com.baby.care.model.Token;
import com.baby.care.repository.ScreenTimeRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScreenTimeServiceTest {

    @Mock
    private ScreenTimeRecordRepository screenTimeRecordRepository;
    @Mock
    private AppUserService appUserService;
    @Mock
    private BabyService babyService;
    @InjectMocks
    private ScreenTimeService screenTimeService;

    private AppUser appUser = new AppUser();
    private ScreenTimeRecord screenTimeRecord1 = new ScreenTimeRecord();
    private ScreenTimeRecord screenTimeRecord2 = new ScreenTimeRecord();
    @BeforeEach
    void setUp() {
        appUser = new AppUser(1L, "user@gmail.com", "password", true, true, true, true, null, List.of(new Token()));

        // Create ScreenTimeRecord
        screenTimeRecord1.setId(1L);
        screenTimeRecord1.setScreenTimeStart(LocalDateTime.of(2024, Month.SEPTEMBER, 7, 10, 0));
        screenTimeRecord1.setScreenTimeEnd(LocalDateTime.of(2024, Month.SEPTEMBER, 7, 12, 0));
        screenTimeRecord1.setBaby(new Baby());
        screenTimeRecord1.setComments("Morning screen time");

        screenTimeRecord2.setId(2L);
        screenTimeRecord2.setScreenTimeStart(LocalDateTime.of(2024, Month.SEPTEMBER, 7, 15, 0));
        screenTimeRecord2.setScreenTimeEnd(LocalDateTime.of(2024, Month.SEPTEMBER, 7, 17, 0));
        screenTimeRecord2.setBaby(new Baby());
        screenTimeRecord2.setComments("Afternoon screen time");
    }

    @Test
    void testGetAllRecordsSuccessfully()
    {
        List<ScreenTimeRecord> screenTimeRecords = List.of(screenTimeRecord1, screenTimeRecord2);

        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.of(appUser));
        when(screenTimeRecordRepository.findAllByBabyIdOrderByScreenTimeStartDesc(anyLong())).thenReturn(screenTimeRecords);

        List<ScreenTimeRecordResponse> responses = screenTimeService.getAllScreenTimeRecords("token", 1L);

        assertEquals(2, responses.size());
        assertEquals(1L, responses.get(0).getId());
        assertEquals(2L, responses.get(1).getId());
    }

    @Test
    void testGetAllRecordsUnsuccessfully_AppUserNotFound()
    {
        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.empty());

        List<ScreenTimeRecordResponse> responses = screenTimeService.getAllScreenTimeRecords("token", 1L);

        assertEquals(0, responses.size());
    }

    @Test
    void testGetAllRecords_NoRecordFound()
    {
        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.of(appUser));
        when(screenTimeRecordRepository.findAllByBabyIdOrderByScreenTimeStartDesc(anyLong())).thenReturn(Collections.emptyList());

        List<ScreenTimeRecordResponse> responses = screenTimeService.getAllScreenTimeRecords("token", 1L);

        assertEquals(0, responses.size());
    }
}