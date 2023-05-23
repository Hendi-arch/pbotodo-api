package com.campus.pbotodo.scheduler.implementation.definition;

import java.time.LocalDateTime;
import org.springframework.lang.Nullable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Schedule30MinutesBeforeEvent implements IScheduleDefinition {
    private String scheduleDefinition;
    private LocalDateTime startTime;

    public Schedule30MinutesBeforeEvent(LocalDateTime eventDateTime) {
        LocalDateTime thirtyMinutesBefore = eventDateTime.minusMinutes(30);
        if (thirtyMinutesBefore.isAfter(LocalDateTime.now())) {
            scheduleDefinition = generateScheduleDefinition(thirtyMinutesBefore);
            log.info("Schedule30MinutesBeforeEvent: {}", scheduleDefinition);
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
