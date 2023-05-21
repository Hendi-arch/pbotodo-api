package com.campus.pbotodo.scheduler.implementation.definition;

import java.time.LocalDateTime;

import org.springframework.lang.Nullable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScheduleAtTimeOfEvent implements ScheduleDefinition {
    private String scheduleDefinition;
    private LocalDateTime startTime;

    public ScheduleAtTimeOfEvent(LocalDateTime eventDateTime) {
        this.scheduleDefinition = eventDateTime.getSecond() + " " + eventDateTime.getMinute() + " "
                + eventDateTime.getHour() + " " + eventDateTime.getDayOfMonth() + " " + eventDateTime.getMonthValue()
                + " ? " + eventDateTime.getYear();
        log.info("ScheduleAtTimeOfEvent: {}", scheduleDefinition);
    }

    @Override
    public String getScheduleDefinition() {
        return scheduleDefinition;
    }

    @Override
    @Nullable
    public LocalDateTime getStartTime() {
        return startTime;
    }
}
