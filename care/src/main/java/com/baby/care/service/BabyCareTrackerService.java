package com.baby.care.service;

import com.baby.care.controller.repsonse.BabyCareTrackerResponse;
import com.baby.care.controller.repsonse.SleepRecordResponse;
import com.baby.care.controller.repsonse.StoolRecordResponse;
import com.baby.care.model.AppUser;
import com.baby.care.model.BabyCareTracker;
import com.baby.care.repository.BabyCareTrackerRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BabyCareTrackerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BabyCareTrackerService.class);

    private BabyCareTrackerRepository babyCareTrackerRepository;
    private BabyService babyService;
    private SleepRecordService sleepRecordService;


    public BabyCareTrackerResponse getCareTracker(Long babyId, String token) {
       Optional<AppUser>  appUserOptional = babyService.isUserAndBabyPresent(token);

       if (appUserOptional.isEmpty()) {
           LOGGER.info("Care Tracker information could not be found.");
           return new BabyCareTrackerResponse();
       }

        Optional<BabyCareTracker> babyCareTrackerOptional = babyCareTrackerRepository.findByBabyId(babyId);

       if (babyCareTrackerOptional.isPresent()) {
           return BabyCareTrackerResponse.builder()
                   .id(babyCareTrackerOptional.get().getId())
                   .date(babyCareTrackerOptional.get().getDate())
                   .sleepRecords(getSleepRecordByBabyCareTrackerId(babyCareTrackerOptional.get().getId()))
                   .stoolRecords(Collections.emptyList())
                   .feedRecords(Collections.emptyList())
                   .build();
       }

        return new BabyCareTrackerResponse();
    }

    private List<SleepRecordResponse> getSleepRecordByBabyCareTrackerId(Long babyCareTrackerId) {
        Optional<List<SleepRecordResponse>> response = sleepRecordService.getSleepRecord(babyCareTrackerId);

        return response.orElse(Collections.emptyList());
    }

}
