package com.baby.care.controller;

import com.baby.care.controller.repsonse.GetAllBabiesResponse;
import com.baby.care.controller.repsonse.SaveBabyResponse;
import com.baby.care.controller.request.SaveBabyRequest;
import com.baby.care.service.BabyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(path = "/baby")
@AllArgsConstructor
public class BabyController {
    private BabyService babyService;

    @PostMapping(path = "/save")
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
    public ResponseEntity<List<GetAllBabiesResponse>> getAllBabies(@RequestHeader("Authorization") String token) {
        try {
            List<GetAllBabiesResponse> response = babyService.getAllBabies(token);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
        }
    }
}
