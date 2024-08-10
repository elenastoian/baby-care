package com.baby.care.repository;

import com.baby.care.model.FeedRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedRecordRepository extends JpaRepository<FeedRecord, Long> {
    List<FeedRecord> findAllByBabyIdOrderByFeedTimeDesc(Long babyId);
}
