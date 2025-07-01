package com.example.timetable.service;

public class ScheduleConflictException extends Exception {
    public ScheduleConflictException(String message) {
        super(message);
    }
}