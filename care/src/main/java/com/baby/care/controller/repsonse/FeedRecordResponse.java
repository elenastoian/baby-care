package com.baby.care.controller.repsonse;

import com.baby.care.model.enums.TypeOfFood;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedRecordResponse {
    private Long id;

    private LocalDateTime feedTime;

    private TypeOfFood typeOfFood;

    private String comments;
}
