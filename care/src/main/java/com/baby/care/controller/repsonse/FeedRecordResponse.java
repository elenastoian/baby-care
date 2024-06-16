package com.baby.care.controller.repsonse;

import com.baby.care.model.enums.TypeOfFood;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class FeedRecordResponse {
    private Long id;

    private LocalDateTime feedTime;

    private TypeOfFood typeOfFood;

    private String comments;

    public FeedRecordResponse(Long id, LocalDateTime feedTime, TypeOfFood typeOfFood, String comments) {
        this.id = id;
        this.feedTime = feedTime;
        this.typeOfFood = typeOfFood;
        this.comments = comments;
    }
}
