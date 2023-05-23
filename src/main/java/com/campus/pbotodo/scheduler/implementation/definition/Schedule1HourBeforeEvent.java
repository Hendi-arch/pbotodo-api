package com.campus.pbotodo.scheduler.implementation.definition;

import java.time.LocalDateTime;
import org.springframework.lang.Nullable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Schedule1HourBeforeEvent implements IScheduleDefinition {
    private String scheduleDefinition;
    private LocalDateTime startTime;

    public Schedule1HourBeforeEvent(LocalDateTime eventDateTime) {
        LocalDateTime oneHourBefore = eventDateTime.minusHours(1);
        if (oneHourBefore.isAfter(LocalDateTime.now())) {
            scheduleDefinition = generateScheduleDefinition(oneHourBefore);
            log.info("Schedule1HourBeforeEvent: {}", scheduleDefinition);
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
