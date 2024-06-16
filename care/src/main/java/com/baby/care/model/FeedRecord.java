package com.baby.care.model;

import com.baby.care.model.enums.TypeOfFood;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class FeedRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime feedTime;

    @Enumerated(EnumType.STRING)
    private TypeOfFood typeOfFood;

    private String comments;

    @ManyToOne
    @JoinColumn(name = "fk_tracker_id", nullable = false)
    private BabyCareTracker tracker;
}
