package com.baby.care.controller;

import com.baby.care.controller.response.SaveUserResponse;
import com.baby.care.controller.request.UpdateUserRequest;
import com.baby.care.service.AppUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class AppUserController {

    private final AppUserService appUserService;

    @PutMapping(path = "/{id}")
    public ResponseEntity<SaveUserResponse> updateAppUser(@Valid @RequestBody UpdateUserRequest updateUserRequest) {
        try {
            SaveUserResponse response = appUserService.updateAppUser(updateUserRequest);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SaveUserResponse());
        }
    }
}
