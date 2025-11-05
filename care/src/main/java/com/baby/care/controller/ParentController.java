package com.baby.care.controller;

import com.baby.care.controller.repsonse.GetParentResponse;
import com.baby.care.controller.repsonse.SaveParentResponse;
import com.baby.care.controller.request.SaveParentRequest;
import com.baby.care.controller.request.UpdateParentRequest;
import com.baby.care.service.ParentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/parents")
@AllArgsConstructor
public class ParentController {

    private ParentService parentService;

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
