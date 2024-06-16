package com.baby.care.repository;

import com.baby.care.model.StoolRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoolRecordRepository extends JpaRepository<StoolRecord, Long> {
    Optional<List<StoolRecord>> findByTrackerId(Long babyCareTrackerId);
}
