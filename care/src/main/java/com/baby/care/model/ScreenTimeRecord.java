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
public class ScreenTimeRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime creationDate;

    private LocalDateTime screenTimeStart;

    private LocalDateTime screenTimeEnd;

    private Duration screenTimeDuration;

    @Column(columnDefinition = "TEXT")
    private String comments;

    @ManyToOne
    @JoinColumn(name = "fk_baby_id", nullable = false)
    private Baby baby;

    @Transient
    public Duration getScreenTimeDuration() {
        if (screenTimeStart != null && screenTimeEnd != null) {
            return Duration.between(screenTimeStart, screenTimeEnd);
        } else {
            return Duration.ZERO;
        }
    }
}
