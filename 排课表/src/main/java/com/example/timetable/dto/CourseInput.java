package com.example.timetable.dto;

import com.example.timetable.model.Schedule;
import lombok.Data;
import java.util.List;

@Data
public class CourseInput {
    private String courseName;
    private String teacherName;
    private String location;
    // 直接接收一个时间安排的列表
    private List<Schedule> schedules;
}