package com.baby.care.controller;

import com.baby.care.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping(path = "/baby/{babyId}")
    public ResponseEntity<?> uploadImage(@PathVariable Long babyId, @RequestParam("image") MultipartFile file, @RequestHeader("Authorization") String token) throws IOException {
        String uploadImage = imageService.uploadImage(babyId, file, token);
        return ResponseEntity.status(HttpStatus.OK).body(uploadImage);
    }
}
