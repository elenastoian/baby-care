package com.baby.care.controller.request;

import com.baby.care.model.enums.Sex;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateParentRequest {
    private String name;
    private LocalDate dateOfBirth;
    private Sex sex;
    private String location;
}
