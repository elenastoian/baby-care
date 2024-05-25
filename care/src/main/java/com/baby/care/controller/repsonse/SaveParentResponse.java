package com.baby.care.controller.repsonse;

import com.baby.care.model.enums.Sex;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SaveParentResponse {
    private Long id;
    private String name;
    private LocalDate dateOfBirth;
    private int age;
    private Sex sex;
    private String location;
}
