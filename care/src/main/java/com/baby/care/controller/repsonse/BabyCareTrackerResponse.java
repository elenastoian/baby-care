package com.baby.care.controller.repsonse;

import jakarta.annotation.Nullable;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BabyCareTrackerResponse {
    private Long id;

    private LocalDate date;

    @Nullable
    private SleepRecordResponse sleepRecords ;

    @Nullable
    private ScreenTimeRecordResponse stoolRecords ;

    @Nullable
    private FeedRecordResponse feedRecords ;
}
