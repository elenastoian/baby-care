package com.baby.care.repository;

import com.baby.care.model.SleepRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SleepRecordRepository extends JpaRepository<SleepRecord, Long> {
    Optional<List<SleepRecord>> findByTrackerId(Long babyCareTrackerId);
}
