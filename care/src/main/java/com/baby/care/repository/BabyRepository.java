package com.baby.care.repository;

import com.baby.care.model.Baby;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BabyRepository extends JpaRepository<Baby, Long> {

}
