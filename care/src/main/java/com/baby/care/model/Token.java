package com.baby.care.model;

import com.baby.care.model.enums.TokenType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "token")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType = TokenType.BEARER;

    private boolean revoked;

    private boolean expired;

    @ManyToOne
    @JoinColumn(name = "user_id" )
    private AppUser user;
}
