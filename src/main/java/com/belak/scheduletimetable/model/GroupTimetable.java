package com.belak.scheduletimetable.model;

import com.belak.scheduletimetable.enumeration.Departement;
import com.belak.scheduletimetable.enumeration.Filiere;
import com.belak.scheduletimetable.enumeration.Semester;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "group_timetable")
public class GroupTimetable {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "group_seq"
    )
    @SequenceGenerator(
            name = "group_seq",
            sequenceName = "group_sequence",
            allocationSize = 1
    )
    private Long id;

    @Enumerated(EnumType.STRING)
    private Departement departement;

    @Enumerated(EnumType.STRING)
    private Filiere filiere;

    private Integer niveau;

    @Column(name = "group_name")
    private String group;

    @Enumerated(EnumType.STRING)
    @Column(name = "semester")
    private Semester semester;

    @Column(name = "position_index", nullable = false)
    private int position;

    private String filename;

    private String contentType;

    @Column(name = "data")
    private byte[] fileData;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "student_timetable",
            joinColumns = @JoinColumn(name = "timetable_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    @Builder.Default
    private Set<Student> students = new HashSet<>();

    @OneToMany(
            mappedBy = "groupTimetable",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<CoursTP> coursTPList = new ArrayList<>();


    public void addStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }

        students.add(student);
        student.getTimetables().add(this);
    }

    public void removeStudent(Student student) {
        if (student == null) {
            return;
        }

        students.remove(student);
        student.getTimetables().remove(this);
    }


    public void addCoursTP(CoursTP coursTP) {
        if (coursTP == null) {
            throw new IllegalArgumentException("CoursTP cannot be null");
        }

        coursTPList.add(coursTP);
        coursTP.setGroupTimetable(this);
    }

    public void removeCoursTP(CoursTP coursTP) {
        if (coursTP == null) {
            return;
        }

        coursTPList.remove(coursTP);
        coursTP.setGroupTimetable(null);
    }
}