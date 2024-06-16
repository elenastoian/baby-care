package com.baby.care.service;

import com.baby.care.controller.repsonse.SleepRecordResponse;
import com.baby.care.controller.repsonse.StoolRecordResponse;
import com.baby.care.model.SleepRecord;
import com.baby.care.model.StoolRecord;
import com.baby.care.repository.StoolRecordRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StoolRecordService {
    private final StoolRecordRepository stoolRecordRepository;

    protected Optional<List<StoolRecordResponse>> getStoolRecord(Long babyCareTrackerId) {
        Optional<List<StoolRecord>> stoolRecordOptional = stoolRecordRepository.findByTrackerId(babyCareTrackerId);

        if (stoolRecordOptional.isPresent()) {
            List<StoolRecordResponse> responseList = stoolRecordOptional.map(stoolRecords ->
                            stoolRecords.stream()
                                    .map(stoolRecord -> new StoolRecordResponse(
                                            stoolRecord.getId(),
                                            stoolRecord.getStoolTime(),
                                            stoolRecord.getConsistency(),
                                            stoolRecord.getComments()
                                    ))
                                    .collect(Collectors.toList())
                    )
                    .orElse(Collections.emptyList());

            return Optional.of(responseList);
        }

        return Optional.of(Collections.emptyList());
    }
}
