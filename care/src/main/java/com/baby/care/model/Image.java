package com.baby.care.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    private String type;

    @Lob
    @Column(name = "bytes", columnDefinition = "bytea")
    private byte[] data;

    @OneToOne(mappedBy = "image")
    private Baby baby;

    public Image(String name, String type, byte[] data, Baby baby) {
        this.name = name;
        this.type = type;
        this.data = data;
        this.baby = baby;
    }
}
