package com.baby.care.repository;

import com.baby.care.model.BabyCareTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BabyCareTrackerRepository extends JpaRepository<BabyCareTracker, Long> {
    Optional<BabyCareTracker> findByBabyId(Long babyId);
}
