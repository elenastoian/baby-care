package com.baby.care.controller.request;

import com.baby.care.model.enums.TypeOfFood;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class FeedRecordRequest {
    private LocalDateTime feedTime;

    private TypeOfFood typeOfFood;

    private String comments;
}
