package com.baby.care.controller.request;

import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SleepRecordRequest {
    private LocalDateTime sleepStart;

    private LocalDateTime sleepEnd;
}
