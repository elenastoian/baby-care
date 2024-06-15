package com.baby.care.model;

import com.baby.care.model.enums.StoolConsistency;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class StoolRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime stoolTime;

    @Enumerated(EnumType.STRING)
    private StoolConsistency consistency;

    @Column(columnDefinition = "TEXT")
    private String comments;

    @ManyToOne
    @JoinColumn(name = "fk_tracker_id", nullable = false)
    private BabyCareTracker tracker;
}
