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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
    private Long id ;
    @Enumerated(EnumType.STRING)
    private Departement departement;
    @Enumerated(EnumType.STRING)
    private Filiere filiere;
    private Integer niveau;
    @Column(name = "group_name")
    private String group;
    @Column(name = "semester")
    @Enumerated(EnumType.STRING)
    private Semester semester ;
    @Column(name = "position_index")
    private int position;
    private String filename ;
    private String contentType ;
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
    public void addStudent(Student student){
        if (students == null) {
            students = new HashSet<>();
        }
        students.add(student);
        student.getTimetables().add(this);
    }

    @OneToMany(mappedBy = "groupTimetable" , fetch = FetchType.LAZY , cascade = CascadeType.ALL , orphanRemoval = true)
    @Builder.Default
    private List<CoursTP> coursTPList = new ArrayList<>();

    public void addCoursTP(CoursTP coursTP){
        if (coursTPList == null) {
            coursTPList = new ArrayList<>();
        }
        coursTPList.add(coursTP);
        coursTP.setGroupTimetable(this);
    }
}
