package com.campus.pbotodo.scheduler.dto;

import org.quartz.JobDetail;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScheduledTasksDto {

    private String taskId;

    private String nextExecutionTime;

    @JsonProperty("status")
    private JobDetail scheduledTask;

}
