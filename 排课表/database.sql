-- ------------------------------------------------------
-- 数据库: `timetable_db`
-- ------------------------------------------------------
CREATE DATABASE IF NOT EXISTS `timetable_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `timetable_db`;

-- ------------------------------------------------------
-- 表结构 `courses` (课程表)
-- ------------------------------------------------------
DROP TABLE IF EXISTS `schedules`;
DROP TABLE IF EXISTS `courses`;

CREATE TABLE `courses` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '课程主键, 自增',
  `course_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '课程名称',
  `teacher_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '教师姓名',
  `location` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '上课地点',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程基本信息表';


-- ------------------------------------------------------
-- 表结构 `schedules` (时间安排表)
-- ------------------------------------------------------
CREATE TABLE `schedules` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '时间安排主键, 自增',
  `course_id` int NOT NULL COMMENT '外键, 关联到courses表的id',
  `day_of_week` int NOT NULL COMMENT '周一(1) 到 周日(7)',
  `start_time` time NOT NULL COMMENT '当天的开始时间, 如 08:00:00',
  `end_time` time NOT NULL COMMENT '当天的结束时间, 如 09:35:00',
  `start_week` int NOT NULL COMMENT '这个时间安排的开始周次',
  `end_week` int NOT NULL COMMENT '这个时间安排的结束周次',
  PRIMARY KEY (`id`),
  KEY `course_id_fk` (`course_id`),
  CONSTRAINT `course_id_fk` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程的具体上课时间安排表';

