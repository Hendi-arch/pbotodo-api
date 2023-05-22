package com.campus.pbotodo.scheduler.implementation.actions.pushnotification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.time.LocalDateTime;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.campus.pbotodo.common.BaseServices;
import com.campus.pbotodo.firebase.messaging.FCMService;
import com.campus.pbotodo.firebase.messaging.PushNotificationConfig;
import com.campus.pbotodo.firebase.messaging.PushNotificationRequest;
import com.campus.pbotodo.scheduler.dto.ScheduleDefinitionDto;
import com.campus.pbotodo.scheduler.dto.ScheduleType;
import com.campus.pbotodo.scheduler.implementation.actions.TaskActionInterface;
import com.campus.pbotodo.scheduler.implementation.service.TaskScheduleService;
import com.campus.pbotodo.security.utils.JwtUtilities;
import com.campus.pbotodo.task.TaskItem;
import com.campus.pbotodo.task.TaskRepo;
import com.campus.pbotodo.task.TaskUrgency;
import com.campus.pbotodo.user.UserToken;
import com.campus.pbotodo.user.UserTokenRepo;
import com.google.firebase.ErrorCode;
import com.google.firebase.messaging.FirebaseMessagingException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class TaskReminderJobDefinition implements TaskActionInterface {

    private UserTokenRepo userTokenRepo;

    private JwtUtilities jwtUtilities;

    private TaskRepo taskRepo;

    private void sendPushNotifications(List<UserToken> userToBeNotify, Long taskId, String taskName, String username,
            String scheduleId, String taskUrgency) {
        for (UserToken user : userToBeNotify) {
            Map<String, String> notificationContentDataFCM = new HashMap<>();
            notificationContentDataFCM.put("taskId", taskId.toString());
            notificationContentDataFCM.put("taskName", taskName);
            notificationContentDataFCM.put("username", username);
            notificationContentDataFCM.put("taskUrgency", taskUrgency);
            notificationContentDataFCM.put("scheduleId", scheduleId);

            // Send notification
            PushNotificationConfig pushNotificationConfig = new PushNotificationConfig();
            pushNotificationConfig.setSound(TaskUrgency.fromValue(taskUrgency).sound);
            pushNotificationConfig.setChannelId(TaskUrgency.fromValue(taskUrgency).value);
            try {
                FCMService.sendMessage(PushNotificationRequest.builder()
                        .title("Task Due Soon - " + taskName)
                        .message("Complete before deadline.")
                        .data(notificationContentDataFCM)
                        .token(user.getDeviceId())
                        .config(pushNotificationConfig)
                        .build());
            } catch (FirebaseMessagingException e) {
                if (e.getErrorCode().equals(ErrorCode.NOT_FOUND) || user.getToken() == null) {
                    jwtUtilities.removeToken(user.getToken());
                }
                e.printStackTrace();
            }
        }
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("TaskReminderScheduleAction.sendPushNotification()");
        final JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

        final Long taskId = jobDataMap.getLong("taskId");
        final String taskName = jobDataMap.getString("taskName");
        final String username = jobDataMap.getString("username");
        final String taskUrgency = jobDataMap.getString("taskUrgency");
        final String scheduleId = jobDataMap.getString("scheduleId");

        List<UserToken> userToBeNotify = userTokenRepo.findActiveDeviceIds(username);

        try {
            sendPushNotifications(userToBeNotify, taskId, taskName, username, scheduleId, taskUrgency);
            context.getScheduler().deleteJob(context.getJobDetail().getKey());
        } catch (Exception e) {
            log.info("Error when run TaskReminderScheduleAction.sendPushNotification() with TaskID"
                    + taskId);
            e.printStackTrace();
        }
    }

    @Override
    public void restore(TaskScheduleService taskScheduleService) {
        log.info("TaskReminderScheduleAction.restore() START");

        List<TaskItem> tasks = taskRepo.findByDone(false);
        tasks.stream()
                .filter(task -> !LocalDateTime.now().isAfter(task.getDueDate()) && !task.isDone())
                .forEach(task -> {
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
                });

        log.info("TaskReminderScheduleAction.restore() END");
    }

}