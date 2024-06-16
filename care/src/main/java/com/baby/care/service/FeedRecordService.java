package com.baby.care.service;

import com.baby.care.controller.repsonse.FeedRecordResponse;
import com.baby.care.controller.repsonse.StoolRecordResponse;
import com.baby.care.model.FeedRecord;
import com.baby.care.model.StoolRecord;
import com.baby.care.repository.FeedRecordRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FeedRecordService {
    private FeedRecordRepository feedRecordRepository;

    protected Optional<List<FeedRecordResponse>> getFeedRecord(Long babyCareTrackerId) {
        Optional<List<FeedRecord>> feedRecordOptional = feedRecordRepository.findByTrackerId(babyCareTrackerId);

        if (feedRecordOptional.isPresent()) {
            List<FeedRecordResponse> responseList = feedRecordOptional.map(feedRecords ->
                            feedRecords.stream()
                                    .map(feedRecord -> new FeedRecordResponse(
                                            feedRecord.getId(),
                                            feedRecord.getFeedTime(),
                                            feedRecord.getTypeOfFood(),
                                            feedRecord.getComments()
                                    ))
                                    .collect(Collectors.toList())
                    )
                    .orElse(Collections.emptyList());

            return Optional.of(responseList);
        }

        return Optional.of(Collections.emptyList());
    }
}
