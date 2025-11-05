package com.baby.care.controller;

import com.baby.care.controller.response.GetParentResponse;
import com.baby.care.controller.response.SaveParentResponse;
import com.baby.care.controller.request.SaveParentRequest;
import com.baby.care.controller.request.UpdateParentRequest;
import com.baby.care.service.ParentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/parents")
@RequiredArgsConstructor
public class ParentController {

    private final ParentService parentService;

    @PostMapping
    public ResponseEntity<SaveParentResponse> saveParent(@RequestBody SaveParentRequest saveParentRequest, @RequestHeader("Authorization") String token) {
        try {
            SaveParentResponse response = parentService.saveParent(saveParentRequest, token);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SaveParentResponse());
        }
    }

    @GetMapping()
    public ResponseEntity<GetParentResponse> getParent(@RequestHeader("Authorization") String token) {
        try {
            GetParentResponse response = parentService.getParent(token);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GetParentResponse());
        }
    }

    @PutMapping(path = "/update")
    public ResponseEntity<SaveParentResponse> updateParent(@RequestBody UpdateParentRequest updateParentRequest, @RequestHeader("Authorization") String token) {
        try {
            SaveParentResponse response = parentService.updateParent(updateParentRequest, token);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SaveParentResponse());
        }
    }
}
