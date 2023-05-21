package com.campus.pbotodo.scheduler.implementation.definition;

import java.time.LocalDateTime;
import org.springframework.lang.Nullable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Schedule15MinutesBeforeEvent implements ScheduleDefinition {
    private String scheduleDefinition;
    private LocalDateTime startTime;

    public Schedule15MinutesBeforeEvent(LocalDateTime eventDateTime) {
        eventDateTime = eventDateTime.minusMinutes(15);
        this.scheduleDefinition = eventDateTime.getSecond() + " " + eventDateTime.getMinute() + " "
                + eventDateTime.getHour() + " " + eventDateTime.getDayOfMonth() + " " + eventDateTime.getMonthValue()
                + " ? " + eventDateTime.getYear();
        log.info("Schedule15MinutesBeforeEvent: {}", scheduleDefinition);
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