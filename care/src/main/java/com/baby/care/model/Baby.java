package com.baby.care.model;

import com.baby.care.model.enums.Sex;
import com.baby.care.model.enums.TypeOfBirth;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Baby {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private LocalDate dateOfBirth;

    @Transient
    private String age;

    @Enumerated(EnumType.STRING)
    @NonNull
    private Sex sex;

    private Double weight;

    private Double height;

    @Enumerated(EnumType.STRING)
    @NonNull
    private TypeOfBirth typeOfBirth;

    private Double birthWeight;

    private String comments;

    @ManyToOne
    @JoinColumn(name = "fk_parent_id", nullable = false)
    @NonNull
    private Parent parent;

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        calculateFormattedAge();
    }

    private void calculateFormattedAge() {
        try {
            LocalDate currentDate = LocalDate.now();
            Period period = Period.between(dateOfBirth, currentDate);
            int years = period.getYears();
            int months = period.getMonths();
            int days = period.getDays();
            age = years + "y " + months + "m " + days + "d";

        } catch (NullPointerException e) {
            Logger LOGGER = LoggerFactory.getLogger(Baby.class);
            LOGGER.info("Baby age could not be calculated due to NullPointerException.");

            age = "";
        }
    }
}
