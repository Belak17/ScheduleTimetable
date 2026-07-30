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
public class ResetToken {

    @Id
    @SequenceGenerator(
            name = "reset_token_seq",
            sequenceName = "reset_token_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "reset_token_sequence"
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

    public ResetToken(String token,
                             LocalDateTime createdAt,
                             LocalDateTime expiresAt,

                             User appUser) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;

        this.appUser=appUser;
    }
}
