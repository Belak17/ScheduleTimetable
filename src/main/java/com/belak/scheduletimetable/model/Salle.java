package com.belak.scheduletimetable.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

    private String code; // S1, A0.1...

    private  byte[] codeQr ;
}
