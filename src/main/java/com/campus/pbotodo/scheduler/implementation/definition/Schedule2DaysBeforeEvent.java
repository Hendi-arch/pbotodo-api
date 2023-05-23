package com.campus.pbotodo.scheduler.implementation.definition;

import java.time.LocalDateTime;
import org.springframework.lang.Nullable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Schedule2DaysBeforeEvent implements IScheduleDefinition {
    private String scheduleDefinition;
    private LocalDateTime startTime;

    public Schedule2DaysBeforeEvent(LocalDateTime eventDateTime) {
        LocalDateTime twoDaysBefore = eventDateTime.minusDays(2);
        if (twoDaysBefore.isAfter(LocalDateTime.now())) {
            scheduleDefinition = generateScheduleDefinition(twoDaysBefore);
            log.info("Schedule2DaysBeforeEvent: {}", scheduleDefinition);
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
