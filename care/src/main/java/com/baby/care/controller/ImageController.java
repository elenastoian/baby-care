package com.baby.care.controller;

import com.baby.care.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(path = "/image")
@AllArgsConstructor
public class ImageController {

    private ImageService imageService;

    @PostMapping(path = "/{babyId}")
    public ResponseEntity<Boolean> saveBaby(@PathVariable Long babyId ,@RequestParam("image") MultipartFile image, @RequestHeader("Authorization") String token) throws IOException {
            return imageService.saveImage(babyId, image, token);
    }
}
