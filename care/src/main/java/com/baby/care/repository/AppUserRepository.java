package com.baby.care.repository;

import com.baby.care.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "UPDATE AppUser a SET a.isEnabled = TRUE WHERE a.email = :email")
    int enableAppUser(@Param("email") String email);

    @Query("SELECT u FROM AppUser u JOIN u.tokens t WHERE t.token = :token")
    Optional<AppUser> findByTokensToken(@Param("token") String token);
}
