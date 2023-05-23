package com.campus.pbotodo.scheduler.implementation.definition;

import java.time.LocalDateTime;

import org.springframework.lang.Nullable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScheduleAtTimeOfEvent implements IScheduleDefinition {
    private String scheduleDefinition;
    private LocalDateTime startTime;

    public ScheduleAtTimeOfEvent(LocalDateTime eventDateTime) {
        scheduleDefinition = generateScheduleDefinition(eventDateTime);
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
