package com.baby.care.controller.request;

import com.baby.care.model.enums.Sex;
import com.baby.care.model.enums.TypeOfBirth;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SaveBabyRequest {
    private String name;
    private LocalDate dateOfBirth;
    private Sex sex;
    private Double weight;
    private Double height;
    private TypeOfBirth typeOfBirth;
    private Double birthWeight;
    private String comments;
}
