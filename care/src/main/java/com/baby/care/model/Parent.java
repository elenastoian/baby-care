package com.baby.care.model;

import com.baby.care.model.enums.Sex;
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
@Builder
@Setter
@Getter
public class Parent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private LocalDate dateOfBirth;

    // age is not persisted in the database;
    // when is needed it will be accessed through its GETTER

    private int age;

    @Enumerated(EnumType.STRING)
    @NonNull
    private Sex sex;

    private String location;

    @OneToOne(mappedBy = "parent")
    @NonNull
    private AppUser appUser;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Baby> babies = new ArrayList<>();

    @Transient
    public int getAge() {
        return calculateAge();
    }

    public int calculateAge() {
        try {
            LocalDate currentDate = LocalDate.now();
            Period period = Period.between(dateOfBirth, currentDate);
            return period.getYears();
        } catch(Exception e) {
            e.getMessage();
            return 0;
        }
    }
}
