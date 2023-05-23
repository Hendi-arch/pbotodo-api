package com.campus.pbotodo.scheduler.implementation.definition;

import java.time.LocalDateTime;
import org.springframework.lang.Nullable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Schedule15MinutesBeforeEvent implements IScheduleDefinition {
    private String scheduleDefinition;
    private LocalDateTime startTime;

    public Schedule15MinutesBeforeEvent(LocalDateTime eventDateTime) {
        LocalDateTime fifTeenMinutesBefore = eventDateTime.minusMinutes(15);
        if (fifTeenMinutesBefore.isAfter(LocalDateTime.now())) {
            scheduleDefinition = generateScheduleDefinition(fifTeenMinutesBefore);
            log.info("Schedule15MinutesBeforeEvent: {}", scheduleDefinition);
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