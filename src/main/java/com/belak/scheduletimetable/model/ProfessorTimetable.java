package com.belak.scheduletimetable.model;

import com.belak.scheduletimetable.enumeration.Grade;
import com.belak.scheduletimetable.enumeration.Statuts;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "professor_timetable")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfessorTimetable {

        @Id
        @GeneratedValue(
                strategy = GenerationType.SEQUENCE,
                generator = "timetable_seq"
        )
        @SequenceGenerator(
                name = "timetable_seq",
                sequenceName = "timetable_sequence",
                allocationSize = 1
        )
        private Long id;

        private String speciality;

        @Enumerated(EnumType.STRING)
        private Statuts statut;

        @Enumerated(EnumType.STRING)
        private Grade grade;

        @Column(name = "position_index", nullable = false)
        private int position;

        private String filename;


        @Column(name = "file_data")
        private byte[] fileData;

        private String contentType;

        @OneToOne(mappedBy = "timetable")
        private Professor professor;
}