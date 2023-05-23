package com.campus.pbotodo.scheduler.implementation.definition;

import java.time.LocalDateTime;
import org.springframework.lang.Nullable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Schedule10MinutesBeforeEvent implements IScheduleDefinition {
    private String scheduleDefinition;
    private LocalDateTime startTime;

    public Schedule10MinutesBeforeEvent(LocalDateTime eventDateTime) {
        LocalDateTime tenMinutesBefore = eventDateTime.minusMinutes(10);
        if (tenMinutesBefore.isAfter(LocalDateTime.now())) {
            scheduleDefinition = generateScheduleDefinition(tenMinutesBefore);
            log.info("Schedule10MinutesBeforeEvent: {}", scheduleDefinition);
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
