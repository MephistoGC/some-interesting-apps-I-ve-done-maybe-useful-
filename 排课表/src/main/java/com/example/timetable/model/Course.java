package com.example.timetable.model;

import lombok.Data;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "courses")
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String courseName;
    private String teacherName;
    private String location;

    // 一门课程可以有多个时间安排
    // CascadeType.ALL: 当保存/删除课程时, 级联保存/删除其所有时间安排
    // orphanRemoval = true: 当从课程的时间安排列表中移除一个元素时, 该元素也会从数据库中删除
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Schedule> schedules;
}
