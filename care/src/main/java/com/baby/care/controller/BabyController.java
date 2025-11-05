package com.baby.care.controller;

import com.baby.care.controller.response.GetBabyResponse;
import com.baby.care.controller.response.SaveBabyResponse;
import com.baby.care.controller.request.SaveBabyRequest;
import com.baby.care.controller.request.UpdateBabyRequest;
import com.baby.care.service.BabyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(path = "/babies")
@AllArgsConstructor
public class BabyController {
    private BabyService babyService;

    @PostMapping
    public ResponseEntity<SaveBabyResponse> saveBaby(@RequestBody SaveBabyRequest saveBabyRequest, @RequestHeader("Authorization") String token)
    {
        try {
            SaveBabyResponse response = babyService.saveBaby(saveBabyRequest, token);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SaveBabyResponse());
        }
    }

    @GetMapping
    public ResponseEntity<List<GetBabyResponse>> getAllBabies(@RequestHeader("Authorization") String token) {
        try {
            List<GetBabyResponse> response = babyService.getAllBabies(token);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<GetBabyResponse> getBaby(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            GetBabyResponse response = babyService.getBaby(id, token);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GetBabyResponse());
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<GetBabyResponse> updateBaby(@PathVariable Long id, @RequestBody UpdateBabyRequest updateBabyRequest, @RequestHeader("Authorization") String token) {
        try {
            GetBabyResponse response = babyService.updateBaby(id, updateBabyRequest, token);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GetBabyResponse());
        }
    }
}
