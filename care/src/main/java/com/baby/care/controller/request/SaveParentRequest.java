package com.baby.care.controller.request;

import com.baby.care.model.enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SaveParentRequest {
    private String name;
    private LocalDate dateOfBirth;
    private Sex sex;
    private String location;
}
