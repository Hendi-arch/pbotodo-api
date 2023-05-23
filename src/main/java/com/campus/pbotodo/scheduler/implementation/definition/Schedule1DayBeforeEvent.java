package com.campus.pbotodo.scheduler.implementation.definition;

import java.time.LocalDateTime;
import org.springframework.lang.Nullable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Schedule1DayBeforeEvent implements IScheduleDefinition {
    private String scheduleDefinition;
    private LocalDateTime startTime;

    public Schedule1DayBeforeEvent(LocalDateTime eventDateTime) {
        LocalDateTime oneDayBefore = eventDateTime.minusDays(1);
        if (oneDayBefore.isAfter(LocalDateTime.now())) {
            scheduleDefinition = generateScheduleDefinition(oneDayBefore);
            log.info("Schedule1DayBeforeEvent: {}", scheduleDefinition);
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
