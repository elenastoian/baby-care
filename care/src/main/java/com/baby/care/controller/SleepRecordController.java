package com.baby.care.controller;

import com.baby.care.controller.response.SleepRecordResponse;
import com.baby.care.controller.request.SaveBabyCareTrackerRequest;
import com.baby.care.service.SleepRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/sleep")
@RequiredArgsConstructor
public class SleepRecordController {
    private final SleepRecordService sleepRecordService;

    @GetMapping(path = "/{babyId}/records")
    public ResponseEntity<List<SleepRecordResponse>> getAllSleepRecords(@PathVariable Long babyId) {
        List<SleepRecordResponse> response = sleepRecordService.getAllSleepRecords(babyId);

        if (!response.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<SleepRecordResponse> saveSleepRecord(@Valid @RequestBody SaveBabyCareTrackerRequest saveBabyCareTrackerRequest) {

        SleepRecordResponse response = sleepRecordService.saveSleepRecord(saveBabyCareTrackerRequest);

        if (response.getId() != null && response.getId() > 0) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
