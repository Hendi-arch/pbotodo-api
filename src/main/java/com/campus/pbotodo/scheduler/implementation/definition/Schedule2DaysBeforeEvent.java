package com.campus.pbotodo.scheduler.implementation.definition;

import java.time.LocalDateTime;
import org.springframework.lang.Nullable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Schedule2DaysBeforeEvent implements ScheduleDefinition {
    private String scheduleDefinition;
    private LocalDateTime startTime;

    public Schedule2DaysBeforeEvent(LocalDateTime eventDateTime) {
        eventDateTime = eventDateTime.minusDays(2);
        this.scheduleDefinition = eventDateTime.getSecond() + " " + eventDateTime.getMinute() + " "
                + eventDateTime.getHour() + " " + eventDateTime.getDayOfMonth() + " " + eventDateTime.getMonthValue()
                + " ? " + eventDateTime.getYear();
        log.info("Schedule2DaysBeforeEvent: {}", scheduleDefinition);
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
