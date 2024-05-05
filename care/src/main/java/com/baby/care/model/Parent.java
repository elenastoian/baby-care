package com.baby.care.model;

import com.baby.care.model.enums.Sex;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

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
}
