package com.campus.pbotodo.scheduler.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScheduleDefinitionDto {
    
    private LocalDateTime alarmTime;

    private ScheduleType scheduleType;

}
