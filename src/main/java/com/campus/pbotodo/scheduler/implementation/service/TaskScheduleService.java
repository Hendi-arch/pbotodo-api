package com.campus.pbotodo.scheduler.implementation.service;

import java.util.List;

import com.campus.pbotodo.scheduler.dto.ScheduledTasksDto;
import com.campus.pbotodo.scheduler.implementation.definition.IScheduleDefinition;

public interface TaskScheduleService {
    
    public <T> void scheduleATask(String jobId, T task, IScheduleDefinition scheduleDefinition);

    public boolean removeScheduledTask(String jobId);

    public List<ScheduledTasksDto> getTasks();

}
