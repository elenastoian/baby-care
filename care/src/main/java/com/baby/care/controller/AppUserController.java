package com.baby.care.controller;

import com.baby.care.controller.repsonse.SaveUserResponse;
import com.baby.care.controller.request.UpdateUserRequest;
import com.baby.care.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/user")
public class AppUserController {

    private AppUserService appUserService;

    @PutMapping(path = "/update")
    public ResponseEntity<SaveUserResponse> updateAppUser(@RequestBody UpdateUserRequest updateUserRequest, @RequestHeader("Authorization") String token) {
        try {
            SaveUserResponse response = appUserService.updateAppUser(updateUserRequest, token);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SaveUserResponse());
        }
    }
}
