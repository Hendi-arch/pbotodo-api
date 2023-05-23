package com.campus.pbotodo.scheduler.implementation.definition;

import java.time.LocalDateTime;
import org.springframework.lang.Nullable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Schedule2HoursBeforeEvent implements IScheduleDefinition {
    private String scheduleDefinition;
    private LocalDateTime startTime;

    public Schedule2HoursBeforeEvent(LocalDateTime eventDateTime) {
        LocalDateTime twoHoursBefore = eventDateTime.minusHours(2);
        if (twoHoursBefore.isAfter(LocalDateTime.now())) {
            scheduleDefinition = generateScheduleDefinition(twoHoursBefore);
            log.info("Schedule2HoursBeforeEvent: {}", scheduleDefinition);
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
