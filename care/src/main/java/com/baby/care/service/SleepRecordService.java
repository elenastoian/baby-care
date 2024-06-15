package com.baby.care.service;

import com.baby.care.controller.repsonse.SleepRecordResponse;
import com.baby.care.model.SleepRecord;
import com.baby.care.repository.SleepRecordRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SleepRecordService {
    private final SleepRecordRepository sleepRecordRepository;

    protected Optional<List<SleepRecordResponse>> getSleepRecord(Long babyCareTrackerId) {
        Optional<List<SleepRecord>> sleepRecordOptional = sleepRecordRepository.findByTrackerId(babyCareTrackerId);

        if (sleepRecordOptional.isPresent()) {
            List<SleepRecordResponse> responseList = sleepRecordOptional.map(sleepRecords ->
                            sleepRecords.stream()
                                    .map(sleepRecord -> new SleepRecordResponse(
                                            sleepRecord.getId(),
                                            sleepRecord.getSleepStart(),
                                            sleepRecord.getSleepEnd(),
                                            sleepRecord.getSleepDuration()
                                    ))
                                    .collect(Collectors.toList())
                    )
                    .orElse(Collections.emptyList());

            return Optional.of(responseList);
        }

        return Optional.of(Collections.emptyList());
    }
}
