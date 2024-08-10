package com.baby.care.controller.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveBabyCareTrackerRequest {

    @NotNull
    private Long babyId;

    @NotNull
    private LocalDate date;

    @Nullable
    private SleepRecordRequest sleepRecord;

    @Nullable
    private StoolRecordRequest stoolRecord;

    @Nullable
    private FeedRecordRequest feedRecord;
}
