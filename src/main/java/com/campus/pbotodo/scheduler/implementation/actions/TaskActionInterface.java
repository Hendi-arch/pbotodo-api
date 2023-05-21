package com.campus.pbotodo.scheduler.implementation.actions;

import org.quartz.Job;

import com.campus.pbotodo.scheduler.implementation.service.TaskScheduleService;

public interface TaskActionInterface extends Job {

    void restore(TaskScheduleService taskScheduleService);

}
