package com.baby.care.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class SleepRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime creationDate;

    private LocalDateTime sleepStart;

    private LocalDateTime sleepEnd;

    private Duration sleepDuration;

    @ManyToOne
    @JoinColumn(name = "fk_baby_id", nullable = false)
    private Baby baby;

    /**
     * Calculates the duration of the sleep period.
     * Do not persist the duration in database
     *
     * @return the duration of sleep as a Duration object.
     */
    @Transient
    public Duration getSleepDuration() {
        if (sleepStart != null && sleepEnd != null) {
            return Duration.between(sleepStart, sleepEnd);
        } else {
            return Duration.ZERO;
        }
    }
}
