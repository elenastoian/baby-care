package com.baby.care.service;

import com.baby.care.model.AppUser;
import com.baby.care.model.Baby;
import com.baby.care.model.Image;
import com.baby.care.repository.BabyRepository;
import com.baby.care.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageService.class);

    private final ImageRepository imageRepository;
    private final BabyRepository babyRepository;
    private final AppUserService appUserService;

    public String uploadImage(Long babyId, MultipartFile imageFile, String token) throws IOException {
        Optional<Baby> babyOptional = babyRepository.findById(babyId);

        if (babyOptional.isEmpty()) {
            return " ";
        }

        Optional<AppUser> appUser = appUserService.findCurrentAppUser(token);

        if (appUser.isEmpty()) {
            return " ";
        }

        LOGGER.info("Image with name {} will be saved for baby with id {}", imageFile.getOriginalFilename(), babyId);

        imageRepository.save(Image.builder()
                .name("baby" + babyOptional.get().getId())
                .type(imageFile.getContentType())
                .data(imageFile.getInputStream().readAllBytes())
                .baby(babyOptional.get())
                .build());

        return "file uploaded successfully : " + imageFile.getOriginalFilename();
    }
}
