package com.baby.care.repository;

import com.baby.care.model.ScreenTimeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScreenTimeRecordRepository extends JpaRepository<ScreenTimeRecord, Long> {

    List<ScreenTimeRecord> findAllByBabyIdOrderByScreenTimeStartDesc(Long babyId);
}
