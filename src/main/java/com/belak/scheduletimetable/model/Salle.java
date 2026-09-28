package com.belak.scheduletimetable.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "salle")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Salle {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "salle_seq"
    )
    @SequenceGenerator(
            name = "salle_seq",
            sequenceName = "salle_sequence",
            allocationSize = 1
    )
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Lob
    @Column(name = "code_qr")
    private byte[] codeQr;
}