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

    private LocalDateTime sleepStart;

    private LocalDateTime sleepEnd;

    private Duration sleepDuration;

    @ManyToOne
    @JoinColumn(name = "tracker_id", nullable = false)
    private BabyCareTracker tracker;

    /**
     * Calculates the duration of the sleep period.
     * @return the duration of sleep as a Duration object.
     */
    public Duration getSleepDuration() {
        if (sleepStart != null && sleepEnd != null) {
            return Duration.between(sleepStart, sleepEnd);
        } else {
            return Duration.ZERO;
        }
    }
}
