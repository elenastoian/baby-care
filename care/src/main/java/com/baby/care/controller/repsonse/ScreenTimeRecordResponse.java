package com.baby.care.controller.repsonse;

import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ScreenTimeRecordResponse {
    private Long id;

    private LocalDateTime screenTimeStart;

    private LocalDateTime screenTimeEnd;

    private Duration screenTimeDuration;

    private String comments;
}
