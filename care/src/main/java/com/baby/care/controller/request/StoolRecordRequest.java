package com.baby.care.controller.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class StoolRecordRequest {
    private LocalDateTime stoolTime;


    private String comments;
}
