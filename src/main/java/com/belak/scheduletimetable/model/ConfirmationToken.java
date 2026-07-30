package com.belak.scheduletimetable.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ConfirmationToken {
    @Id
    @SequenceGenerator(
            name = "confirmation_token_seq",
            sequenceName = "confirmation_token_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "confirmation_token_sequence"
    )
    private Long id ;
    @Column(nullable = false)
    private String token ;
    @Column(nullable = false)
    private LocalDateTime createdAt ;

    @Column(nullable = false)
    private  LocalDateTime expiresAt ;
    @Column(nullable = true)
    private LocalDateTime confirmedAt ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            nullable = false ,
            name = "app_user_id"
    )
    private User appUser ;

    public ConfirmationToken(String token,
                             LocalDateTime createdAt,
                             LocalDateTime expiresAt,

                             User appUser) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;

        this.appUser=appUser;
    }
}
