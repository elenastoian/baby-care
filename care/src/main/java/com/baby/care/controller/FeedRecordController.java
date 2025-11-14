package com.baby.care.controller;

import com.baby.care.controller.response.FeedRecordResponse;
import com.baby.care.service.FeedRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/feeding")
@RequiredArgsConstructor
public class FeedRecordController {

    private final FeedRecordService feedRecordService;

    @GetMapping(path = "/{babyId}/records")
    public ResponseEntity<List<FeedRecordResponse>> getAllFeedRecords(@PathVariable Long babyId) {
        List<FeedRecordResponse> response = feedRecordService.getAllFeedRecords(babyId);

        return response.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(response);
    }
}
