package com.campus.pbotodo.scheduler.implementation.definition;

import java.time.LocalDateTime;

import org.springframework.lang.Nullable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Schedule5MinutesBeforeEvent implements ScheduleDefinition {
    private String scheduleDefinition;
    private LocalDateTime startTime;

    public Schedule5MinutesBeforeEvent(LocalDateTime eventDateTime) {
        eventDateTime = eventDateTime.minusMinutes(5);
        this.scheduleDefinition = eventDateTime.getSecond() + " " + eventDateTime.getMinute() + " "
                + eventDateTime.getHour() + " " + eventDateTime.getDayOfMonth() + " " + eventDateTime.getMonthValue()
                + " ? " + eventDateTime.getYear();
        log.info("Schedule5MinutesBeforeEvent: {}", scheduleDefinition);
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
