package com.baby.care.controller.repsonse;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BabyCareTrackerResponse {
    private Long id;

    private LocalDate date;

    private List<SleepRecordResponse> sleepRecords = new ArrayList<>();

    private List<StoolRecordResponse> stoolRecords = new ArrayList<>();

    private List<FeedRecordResponse> feedRecords = new ArrayList<>();
}
