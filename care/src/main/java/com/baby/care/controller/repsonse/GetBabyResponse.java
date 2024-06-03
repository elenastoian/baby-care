package com.baby.care.controller.repsonse;

import com.baby.care.model.enums.Sex;
import com.baby.care.model.enums.TypeOfBirth;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GetBabyResponse {

    private Long id;

    private String name;

    private LocalDate dateOfBirth;

    private String age;

    private Sex sex;

    private Double weight;

    private Double height;

    private TypeOfBirth typeOfBirth;

    private Double birthWeight;

    private String comments;
}
