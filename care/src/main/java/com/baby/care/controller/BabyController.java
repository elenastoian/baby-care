package com.baby.care.controller;

import com.baby.care.controller.repsonse.SaveBabyResponse;
import com.baby.care.controller.request.SaveBabyRequest;
import com.baby.care.service.BabyService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
