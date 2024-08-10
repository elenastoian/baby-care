package com.baby.care.repository;

import com.baby.care.model.SleepRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SleepRecordRepository extends JpaRepository<SleepRecord, Long> {

    List<SleepRecord> findAllByBabyIdOrderBySleepStartDesc(Long babyId);
}
