package com.baby.care.controller;

import com.baby.care.controller.repsonse.SaveParentResponse;
import com.baby.care.controller.request.SaveParentRequest;
import com.baby.care.service.ParentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/v1/parent")
@AllArgsConstructor
public class ParentController {
    private final ParentService parentService;

    /**
     * Create parent profile
     * The parent information will be required, but a Baby will not be saved within this request
     * If Parent could not be saved, throw exception and status 400
     *
     * @param saveParentRequest information about parent
     * @param token created when the user authenticates to an account
     * @return the saved information about the parent
     */
    @PostMapping(path = "/save")
    public ResponseEntity<SaveParentResponse> saveParent(@RequestBody SaveParentRequest saveParentRequest, @RequestHeader("Authorization") String token) {
        try {
            SaveParentResponse response = parentService.saveParent(saveParentRequest, token);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SaveParentResponse());
        }
    }
}
