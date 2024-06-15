package com.baby.care.controller.request;

import com.baby.care.model.enums.Sex;
import com.baby.care.model.enums.TypeOfBirth;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateBabyRequest {
    private Long id;
    private String name;
    private LocalDate dateOfBirth;
    private Sex sex;
    private Double weight;
    private Double height;
    private TypeOfBirth typeOfBirth;
    private Double birthWeight;
    private String comments;
}
