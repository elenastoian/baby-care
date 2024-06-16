package com.baby.care.controller;

import com.baby.care.controller.repsonse.BabyCareTrackerResponse;
import com.baby.care.service.BabyCareTrackerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/track")
@AllArgsConstructor
public class BabyCareTrackerController {
    private final BabyCareTrackerService service;

    @GetMapping(path = "{babyId}")
    public ResponseEntity<BabyCareTrackerResponse> getCareTracker(@PathVariable Long babyId, @RequestHeader("Authorization") String token) {
        try {
            BabyCareTrackerResponse response = service.getCareTracker(babyId, token);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BabyCareTrackerResponse());
        }
    }
}
