package com.example.timetable.repository;

import com.example.timetable.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalTime; // 引入新的时间类
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

    @Query("SELECT s FROM Schedule s WHERE s.dayOfWeek = :dayOfWeek " +
            "AND s.startTime < :endTime AND s.endTime > :startTime " +
            "AND :week BETWEEN s.startWeek AND s.endWeek")
    List<Schedule> findConflictingSchedules(
            @Param("dayOfWeek") int dayOfWeek,
            @Param("startTime") LocalTime startTime, // 更新参数类型
            @Param("endTime") LocalTime endTime,     // 更新参数类型
            @Param("week") int week
    );
}