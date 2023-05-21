package com.campus.pbotodo.task;

import java.util.HashMap;
import java.util.Map;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.campus.pbotodo.common.BaseServices;
import com.campus.pbotodo.scheduler.dto.ScheduleDefinitionDto;
import com.campus.pbotodo.scheduler.dto.ScheduleType;
import com.campus.pbotodo.scheduler.implementation.actions.pushnotification.TaskReminderJobDefinition;
import com.campus.pbotodo.scheduler.implementation.service.TaskScheduleService;

@Service
public class TaskService {

        @Autowired
        private TaskScheduleService taskScheduleService;

        public void scheduleTaskReminder(TaskItem task) {
                Map<String, Object> taskDefinitionData = new HashMap<>();
                taskDefinitionData.put("taskId", task.getId());
                taskDefinitionData.put("taskName", task.getTitle());
                taskDefinitionData.put("username", task.getUsername());
                taskDefinitionData.put("taskUrgency", task.getUrgency());
                taskDefinitionData.put("scheduleId", BaseServices.getTaskId(task.getId(), task.getUsername()));
                taskDefinitionData.put("reminderScheduleId",
                                BaseServices.getReminderTaskId(task.getId(), task.getUsername()));

                JobDetail jobDetail = JobBuilder.newJob()
                                .withIdentity(taskDefinitionData.get("scheduleId").toString())
                                .withDescription("Notify user " + task.getUsername()
                                                + " for Task Due Soon " + task.getTitle())
                                .setJobData(new JobDataMap(taskDefinitionData))
                                .ofType(TaskReminderJobDefinition.class)
                                .build();

                JobDetail reminderJobDetail = JobBuilder.newJob()
                                .withIdentity(taskDefinitionData.get("reminderScheduleId").toString())
                                .withDescription("Notify user " + task.getUsername()
                                                + " for Task " + task.getTitle())
                                .setJobData(new JobDataMap(taskDefinitionData))
                                .ofType(TaskReminderJobDefinition.class)
                                .build();

                taskScheduleService.scheduleATask(
                                taskDefinitionData.get("scheduleId").toString(), jobDetail,
                                BaseServices.getScheduleDefinition(new ScheduleDefinitionDto(
                                                task.getDueDate(), ScheduleType.AT_TIME)));

                taskScheduleService.scheduleATask(
                                taskDefinitionData.get("reminderScheduleId").toString(), reminderJobDetail,
                                BaseServices.getScheduleDefinition(new ScheduleDefinitionDto(
                                                task.getDueDate(),
                                                ScheduleType.fromValue(task.getNotificationChannelId()))));
        }

        public void removeScheduleTaskReminder(long id, String username) {
                taskScheduleService.removeScheduledTask(BaseServices.getTaskId(id, username));
                taskScheduleService.removeScheduledTask(BaseServices.getReminderTaskId(id, username));
        }

}
