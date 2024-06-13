package com.baby.care.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String type;

    @Lob
    //@Column(name = "bytes")
    private byte[] data;

    @OneToOne(mappedBy = "image")
    private Baby baby;

    private LocalDateTime timeAdded;
}
