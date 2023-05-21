package com.campus.pbotodo.scheduler.implementation.service;

import java.util.List;

import com.campus.pbotodo.scheduler.dto.ScheduledTasksDto;
import com.campus.pbotodo.scheduler.implementation.definition.ScheduleDefinition;

public interface TaskScheduleService {
    
    public <T> void scheduleATask(String jobId, T task, ScheduleDefinition scheduleDefinition);

    public boolean removeScheduledTask(String jobId);

    public List<ScheduledTasksDto> getTasks();

}
