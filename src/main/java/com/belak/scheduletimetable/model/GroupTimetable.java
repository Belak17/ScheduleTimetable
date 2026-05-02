package com.belak.scheduletimetable.model;

import com.belak.scheduletimetable.enumeration.Departement;
import com.belak.scheduletimetable.enumeration.Filiere;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "position_index")
    private int position;
    private String filename ;
    private String contentType ;
    @Column(name = "data")
    private byte[] fileData;
    @OneToMany(mappedBy = "groupTimetable" , fetch = FetchType.LAZY)
    private List<Student> students = new ArrayList<>();
    public void addStudent(Student student){
        if (students == null) {
            students = new ArrayList<>();
        }
        students.add(student);
        student.setGroupTimetable(this);
    }

    @OneToMany(mappedBy = "groupTimetable" , fetch = FetchType.LAZY , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<CoursTP> coursTPList = new ArrayList<>();

    public void addCoursTP(CoursTP coursTP){
        if (coursTPList == null) {
            coursTPList = new ArrayList<>();
        }
        coursTPList.add(coursTP);
        coursTP.setGroupTimetable(this);
    }
}
