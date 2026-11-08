package com.belak.scheduletimetable.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
        name = "reset_tokens",
        indexes = {
                @Index(
                        name = "idx_reset_token_user",
                        columnList = "app_user_id"
                )
        }
)
public class ResetToken {

    @Id
    @SequenceGenerator(
            name = "reset_token_seq",
            sequenceName = "reset_token_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "reset_token_seq"
    )
    private Long id;

    @Column(
            nullable = false,
            unique = true,
            length = 100
    )
    private String token;

    @Column(
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column
    private LocalDateTime confirmedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            nullable = false,
            name = "app_user_id"
    )
    private User appUser;


    public ResetToken(
            String token,
            LocalDateTime createdAt,
            LocalDateTime expiresAt,
            User appUser
    ) {

        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.appUser = appUser;
    }
}