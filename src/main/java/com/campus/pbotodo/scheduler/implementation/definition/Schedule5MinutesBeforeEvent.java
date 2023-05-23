package com.campus.pbotodo.scheduler.implementation.definition;

import java.time.LocalDateTime;

import org.springframework.lang.Nullable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Schedule5MinutesBeforeEvent implements IScheduleDefinition {
    private String scheduleDefinition;
    private LocalDateTime startTime;

    public Schedule5MinutesBeforeEvent(LocalDateTime eventDateTime) {
        LocalDateTime fiveMinutesBefore = eventDateTime.minusMinutes(5);
        if (fiveMinutesBefore.isAfter(LocalDateTime.now())) {
            scheduleDefinition = generateScheduleDefinition(fiveMinutesBefore);
            log.info("Schedule5MinutesBeforeEvent: {}", scheduleDefinition);
        }
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
