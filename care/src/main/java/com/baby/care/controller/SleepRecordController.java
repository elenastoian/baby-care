package com.baby.care.controller;

import com.baby.care.controller.repsonse.SleepRecordResponse;
import com.baby.care.controller.request.SaveBabyCareTrackerRequest;
import com.baby.care.service.SleepRecordService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/sleep")
@AllArgsConstructor
public class SleepRecordController {
    private SleepRecordService sleepRecordService;

    @GetMapping(path = "/get-all/baby/{babyId}")
    public ResponseEntity<List<SleepRecordResponse>> getAllSleepRecords(@RequestHeader("Authorization") String token, @PathVariable Long babyId) {
        List<SleepRecordResponse> response = sleepRecordService.getAllSleepRecords(token, babyId);

        if (!response.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping(path = "/save")
    public ResponseEntity<SleepRecordResponse> saveSleepRecord(@RequestHeader("Authorization") String token,
                                                               @Valid @RequestBody SaveBabyCareTrackerRequest saveBabyCareTrackerRequest) {

        SleepRecordResponse response = sleepRecordService.saveSleepRecord(token, saveBabyCareTrackerRequest);

        if (response.getId() != null && response.getId() > 0) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
