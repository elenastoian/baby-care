package com.baby.care.controller.repsonse;

import com.baby.care.model.enums.StoolConsistency;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class StoolRecordResponse {
    private Long id;

    private LocalDateTime stoolTime;

    private StoolConsistency consistency;

    private String comments;

    public StoolRecordResponse(Long id, LocalDateTime stoolTime, StoolConsistency consistency, String comments) {
        this.id = id;
        this.stoolTime = stoolTime;
        this.consistency = consistency;
        this.comments = comments;
    }
}
