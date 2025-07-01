package com.example.timetable.controller;

import com.example.timetable.dto.CourseInput;
import com.example.timetable.service.CourseService;
import com.example.timetable.service.ScheduleConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Calendar;

@Controller
public class TimetableController {

    @Autowired
    private CourseService courseService;

    /**
     * [已更新]
     * 显示主页, 并根据请求参数显示特定周的课表。
     * @param week 'week'请求参数, 可选。如果不存在, 默认为1。
     * @param model Spring MVC模型
     * @return 视图名称
     */
    @GetMapping("/")
    public String viewHomePage(@RequestParam(value = "week", required = false, defaultValue = "1") int week, Model model) {
        // 防止周数小于1
        int currentWeek = Math.max(week, 1);

        // 调用Service层获取处理好的、用于展示的周课表数据
        model.addAttribute("weeklyCourses", courseService.getCoursesForWeek(currentWeek));
        // 将当前周数也传递给前端, 用于显示和翻页
        model.addAttribute("currentWeek", currentWeek);

        return "index"; // 返回 index.html
    }

    // 处理添加新课程的请求 (保持不变)
    @PostMapping("/courses/add")
    public String addCourse(CourseInput courseInput, RedirectAttributes redirectAttributes) {
        try {
            courseService.addCourse(courseInput);
            redirectAttributes.addFlashAttribute("successMessage", "课程添加成功！");
        } catch (ScheduleConflictException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/";
    }
}