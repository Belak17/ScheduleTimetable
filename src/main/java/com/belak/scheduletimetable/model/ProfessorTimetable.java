package com.belak.scheduletimetable.model;

import com.belak.scheduletimetable.enumeration.Grade;
import com.belak.scheduletimetable.enumeration.Statuts;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "professor_timetable")
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
        private Long id ;
        private String speciality ;
        private Statuts statut ;
        private Grade grade ;
        @Column(name = "position_index")
        private int position;
        private String filename ;

        @Column( name = "file_data")
        private byte[] fileData;
        private String contentType;
        // côté inverse
        @OneToOne(mappedBy = "timetable")
        private Professor professor;

    }
