package com.baby.care.service;

import com.baby.care.model.Baby;
import com.baby.care.model.Image;
import com.baby.care.repository.ImageRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ImageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageService.class);

    private ImageRepository imageRepository;
    private final BabyService babyService;

    @Transactional
    public ResponseEntity<Boolean> saveImage(Long babyId, MultipartFile file, String token) throws IOException {

            Optional<Baby> babyOptional = babyService.findBabyById(babyId);

            if (babyOptional.isEmpty()) {
                return ResponseEntity.ok().body(false);
            }

            Baby baby = babyOptional.get();

            Image imageSaved = imageRepository.save(Image.builder()
                    .name("baby-" + babyOptional.get().getId())
                    .type(file.getContentType())
                    .data(file.getInputStream().readAllBytes())
                    .baby(baby)
                    .timeAdded(LocalDateTime.now())
                    .build());

            babyService.saveBaby(babyOptional.get());

           if (imageSaved.getId() != null) {
               baby.setImage(imageSaved);

               babyService.saveBaby(babyOptional.get());

               LOGGER.info("ImageService - saveImage - image was saved");

               return ResponseEntity.ok().body(true);
           }

        return ResponseEntity.ok().body(false);
    }
}
