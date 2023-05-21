package com.campus.pbotodo.scheduler.implementation.definition;

import java.time.LocalDateTime;

import org.springframework.lang.Nullable;

/**
 ┌───────────── second (0-59)
 │ ┌───────────── minute (0 - 59)
 │ │ ┌───────────── hour (0 - 23)
 │ │ │ ┌───────────── day of the month (1 - 31)
 │ │ │ │ ┌───────────── month (1 - 12) (or JAN-DEC)
 │ │ │ │ │ ┌───────────── day of the week (0 - 7)
 │ │ │ │ │ │          (or MON-SUN -- 0 or 7 is Sunday)
 │ │ │ │ │ │
 * * * * * *
 */
public interface ScheduleDefinition {

    String getScheduleDefinition();

    @Nullable
    LocalDateTime getStartTime();

}
