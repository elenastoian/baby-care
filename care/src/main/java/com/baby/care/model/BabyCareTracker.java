package com.baby.care.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "care_tracker")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class BabyCareTracker {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDate date;

    @OneToMany(mappedBy = "tracker", cascade = CascadeType.ALL)
    private List<SleepRecord> sleepRecords = new ArrayList<>();

    @OneToMany(mappedBy = "tracker", cascade = CascadeType.ALL)
    private List<StoolRecord> stoolRecords = new ArrayList<>();

    @OneToMany(mappedBy = "tracker", cascade = CascadeType.ALL)
    private List<FeedRecord> feedRecords = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "fk_baby_id", nullable = false)
    private Baby baby;
}
