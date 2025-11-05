package com.baby.care.controller.response;

import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SleepRecordResponse {
    private Long id;

    private LocalDateTime sleepStart;

    private LocalDateTime sleepEnd;

    private Duration sleepDuration;

}
