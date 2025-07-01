package com.example.timetable.service;

import com.example.timetable.dto.CourseInput;
import com.example.timetable.model.Course;
import com.example.timetable.model.Schedule;
import com.example.timetable.repository.CourseRepository;
import com.example.timetable.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime; // 引入新的时间类
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Transactional
    public Course addCourse(CourseInput courseInput) throws ScheduleConflictException {
        for (Schedule newSchedule : courseInput.getSchedules()) {
            for (int week = newSchedule.getStartWeek(); week <= newSchedule.getEndWeek(); week++) {
                List<Schedule> conflicts = scheduleRepository.findConflictingSchedules(
                        newSchedule.getDayOfWeek(),
                        newSchedule.getStartTime(),
                        newSchedule.getEndTime(),
                        week
                );
                if (!conflicts.isEmpty()) {
                    Schedule existingSchedule = conflicts.get(0);
                    String errorMessage = String.format(
                            "课程添加失败！在第 %d 周，星期%d 的 %s-%s 时间段与现有课程《%s》发生冲突。",
                            week,
                            newSchedule.getDayOfWeek(),
                            newSchedule.getStartTime().toString(),
                            newSchedule.getEndTime().toString(),
                            existingSchedule.getCourse().getCourseName()
                    );
                    throw new ScheduleConflictException(errorMessage);
                }
            }
        }

        Course course = new Course();
        course.setCourseName(courseInput.getCourseName());
        course.setTeacherName(courseInput.getTeacherName());
        course.setLocation(courseInput.getLocation());
        for (Schedule schedule : courseInput.getSchedules()) {
            schedule.setCourse(course);
        }
        course.setSchedules(courseInput.getSchedules());
        return courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Map<Integer, Map<Integer, List<Course>>> getCoursesForWeek(int weekNumber) {
        Map<Integer, Map<Integer, List<Course>>> weeklyCourses = new HashMap<>();
        for (int day = 1; day <= 7; day++) {
            weeklyCourses.put(day, new HashMap<>());
            for (int section = 1; section <= 12; section++) {
                weeklyCourses.get(day).put(section, new ArrayList<>());
            }
        }

        List<Course> allCourses = courseRepository.findAll();
        for (Course course : allCourses) {
            for (Schedule schedule : course.getSchedules()) {
                if (weekNumber >= schedule.getStartWeek() && weekNumber <= schedule.getEndWeek()) {
                    int startSection = timeToSection(schedule.getStartTime());
                    int endSection = timeToSection(schedule.getEndTime());
                    for (int section = startSection; section <= endSection; section++) {
                        weeklyCourses.get(schedule.getDayOfWeek()).get(section).add(course);
                    }
                }
            }
        }
        return weeklyCourses;
    }

    /**
     * [已修复]
     * 辅助方法的参数类型更新为 LocalTime。
     * @param localTime 时间
     * @return 对应的节次 (1-12)
     */
    private int timeToSection(LocalTime localTime) {
        if (localTime.isBefore(LocalTime.of(8, 51))) return 1;
        if (localTime.isBefore(LocalTime.of(9, 51))) return 2;
        if (localTime.isBefore(LocalTime.of(10, 51))) return 3;
        if (localTime.isBefore(LocalTime.of(11, 51))) return 4;
        if (localTime.isBefore(LocalTime.of(13, 51))) return 5;
        if (localTime.isBefore(LocalTime.of(14, 51))) return 6;
        if (localTime.isBefore(LocalTime.of(15, 51))) return 7;
        if (localTime.isBefore(LocalTime.of(16, 51))) return 8;
        if (localTime.isBefore(LocalTime.of(18, 51))) return 9;
        if (localTime.isBefore(LocalTime.of(19, 51))) return 10;
        if (localTime.isBefore(LocalTime.of(20, 51))) return 11;
        return 12;
    }
}
