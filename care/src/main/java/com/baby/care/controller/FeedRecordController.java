package com.baby.care.controller;

import com.baby.care.controller.repsonse.FeedRecordResponse;
import com.baby.care.service.FeedRecordService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/feed")
@AllArgsConstructor
public class FeedRecordController {

    private FeedRecordService feedRecordService;

    @GetMapping(path = "/get-all/baby/{babyId}")
    public ResponseEntity<List<FeedRecordResponse>> getAllSleepRecords(@RequestHeader("Authorization") String token, @PathVariable Long babyId) {
        List<FeedRecordResponse> response = feedRecordService.getAllFeedRecords(token, babyId);

        if (!response.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
