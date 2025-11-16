package com.baby.care.controller;

import com.baby.care.controller.response.ScreenTimeRecordResponse;
import com.baby.care.service.ScreenTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/screen")
@RequiredArgsConstructor
public class ScreenTimeRecordController {
    private final ScreenTimeService screenTimeService;

    @GetMapping(path = "/{babyId}/records")
    public ResponseEntity<List<ScreenTimeRecordResponse>> getScreenRecords(@PathVariable Long babyId) {
        List<ScreenTimeRecordResponse> response = screenTimeService.getScreenRecords(babyId);

        if (!response.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}