package com.baby.care.controller;

import com.baby.care.controller.response.FeedRecordResponse;
import com.baby.care.service.FeedRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/feeding")
@RequiredArgsConstructor
public class FeedRecordController {

    private final FeedRecordService feedRecordService;

    @GetMapping(path = "/{babyId}/records")
    public ResponseEntity<List<FeedRecordResponse>> getAllFeedRecords(@RequestHeader("Authorization") String token, @PathVariable Long babyId) {
        List<FeedRecordResponse> response = feedRecordService.getAllFeedRecords(token, babyId);

        if (!response.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
