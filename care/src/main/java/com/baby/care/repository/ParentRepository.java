package com.baby.care.repository;

import com.baby.care.model.AppUser;
import com.baby.care.model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParentRepository extends JpaRepository<Parent, Long> {
    Optional<Parent> findByAppUser(AppUser appUser);
}
