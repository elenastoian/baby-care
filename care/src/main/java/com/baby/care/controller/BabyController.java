package com.baby.care.controller;

import com.baby.care.controller.repsonse.GetBabyResponse;
import com.baby.care.controller.repsonse.SaveBabyResponse;
import com.baby.care.controller.request.SaveBabyRequest;
import com.baby.care.service.BabyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(path = "/baby")
@AllArgsConstructor
public class BabyController {
    private BabyService babyService;

    @PostMapping(value = "/save")
    public ResponseEntity<SaveBabyResponse> saveBaby(@RequestBody SaveBabyRequest saveBabyRequest, @RequestHeader("Authorization") String token)
    {
        try {
            SaveBabyResponse response = babyService.saveBaby(saveBabyRequest, token);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SaveBabyResponse());
        }
    }

    @GetMapping(path = "/all")
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
}
