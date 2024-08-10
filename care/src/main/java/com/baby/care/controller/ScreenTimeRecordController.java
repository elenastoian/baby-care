package com.baby.care.controller;

import com.baby.care.controller.repsonse.ScreenTimeRecordResponse;
import com.baby.care.service.ScreenTimeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/screen")
@AllArgsConstructor
public class ScreenTimeRecordController {
    private ScreenTimeService screenTimeService;

    @GetMapping(path = "/get-all/baby/{babyId}")
    public ResponseEntity<List<ScreenTimeRecordResponse>> getAllSleepRecords(@RequestHeader("Authorization") String token, @PathVariable Long babyId) {
        List<ScreenTimeRecordResponse> response = screenTimeService.getAllScreenTimeRecords(token, babyId);

        if (!response.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}