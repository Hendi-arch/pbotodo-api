package com.campus.pbotodo.scheduler.implementation.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.DateBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import com.campus.pbotodo.scheduler.implementation.definition.IScheduleDefinition;
import com.campus.pbotodo.scheduler.dto.ScheduledTasksDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("quartzSchedulerService")
final class QuartzScheduleServiceImpl implements TaskScheduleService {

    @Autowired
    @Qualifier("quartzScheduler")
    private Scheduler taskScheduler;

    final List<ScheduledTasksDto> jobs = new ArrayList<>();

    @Override
    public <T> void scheduleATask(String jobId, T task, IScheduleDefinition scheduleDefinition) {
        if (scheduleDefinition != null) {
            removeScheduledTask(jobId);

            final LocalDateTime startTime = scheduleDefinition.getStartTime();
            final String cronScheduleDefinition = scheduleDefinition.getScheduleDefinition();
            if (startTime != null) {
                final Calendar calendar = Calendar.getInstance();
                final Date startAt = Date
                        .from(startTime.atZone(ZoneId.systemDefault()).toInstant());

                calendar.setTime(startAt);
                final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                final int minute = calendar.get(Calendar.MINUTE);
                final int second = calendar.get(Calendar.SECOND);
                final int dom = calendar.get(Calendar.DAY_OF_MONTH);
                final int month = calendar.get(Calendar.MONTH) + 1;
                final int year = calendar.get(Calendar.YEAR);

                final Date dateNow = new Date();
                final Date startAtBuilder = DateBuilder.dateOf(hour, minute, second, dom, month, year);
                log.info("Is Date before startAt : " + dateNow.before(startAt));
                log.info("Date : " + dateNow + ". Start at : " + startAt + ". Start at Builder : " + startAtBuilder);
                log.info("Hour : " + hour + ". Minute : " + minute + ". Second : " + second + ". DOM : " + dom
                        + ". Month : " + month + ". Year : " + year);

                CronTrigger cron;
                if (new Date().before(startAt)) {
                    cron = TriggerBuilder.newTrigger()
                            .withSchedule(
                                    CronScheduleBuilder.cronSchedule(cronScheduleDefinition)
                                            .inTimeZone(TimeZone.getDefault()))
                            .startAt(DateBuilder.dateOf(hour, minute, second, dom, month, year))
                            .build();
                } else {
                    cron = TriggerBuilder.newTrigger()
                            .withSchedule(
                                    CronScheduleBuilder.cronSchedule(cronScheduleDefinition)
                                            .inTimeZone(TimeZone.getDefault()))
                            .build();
                }

                scheduleJob(jobId, cron, (JobDetail) task);
            } else if (cronScheduleDefinition != null) {
                CronTrigger cron = TriggerBuilder.newTrigger()
                        .withSchedule(
                                CronScheduleBuilder.cronSchedule(cronScheduleDefinition)
                                        .inTimeZone(TimeZone.getDefault()))
                        .build();

                scheduleJob(jobId, cron, (JobDetail) task);
            }
        } else {
            log.info("Schedule Definition cannot be null.");
        }
    }

    @Override
    public boolean removeScheduledTask(String jobId) {
        final ScheduledTasksDto scheduledTask = findTaskById(jobId);
        if (scheduledTask != null) {
            boolean isJobDeleted = false;
            try {
                log.info("Active tasks before canceled: " + taskScheduler.getCurrentlyExecutingJobs().size());
                isJobDeleted = taskScheduler.deleteJob(scheduledTask.getScheduledTask().getKey());
                log.info("Active tasks after canceled: " + taskScheduler.getCurrentlyExecutingJobs().size());
                jobs.removeIf(task -> task.getTaskId().equals(scheduledTask.getTaskId()));
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
            return isJobDeleted;
        } else {
            log.info("No task found with job id: " + jobId);
            return false;
        }
    }

    @Override
    public List<ScheduledTasksDto> getTasks() {
        final List<ScheduledTasksDto> currentlyExecutingJobs = new ArrayList<>();
        try {
            for (String groupName : taskScheduler.getJobGroupNames()) {
                for (JobKey jobKey : taskScheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    final String jobName = jobKey.getName();
                    final JobDetail jobDetail = taskScheduler.getJobDetail(jobKey);
                    final Date triggers = taskScheduler.getTriggersOfJob(jobKey).get(0).getNextFireTime();

                    currentlyExecutingJobs.add(ScheduledTasksDto.builder()
                            .taskId(jobName)
                            .scheduledTask(jobDetail)
                            .nextExecutionTime(triggers.toString())
                            .build());
                }
            }
            return currentlyExecutingJobs;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Nullable
    private ScheduledTasksDto findTaskById(String taskId) {
        return jobs.stream()
                .filter(task -> task.getTaskId().equals(taskId)).findFirst().orElse(null);
    }

    private void scheduleJob(String jobId, CronTrigger cron, JobDetail jobDetail) {
        try {
            taskScheduler.scheduleJob(jobDetail, cron);
            log.info("Scheduling task with job id: " + jobId);
            log.info("Next execution time : " + cron.getNextFireTime().toString());
            jobs.add(ScheduledTasksDto.builder()
                    .taskId(jobId)
                    .scheduledTask(jobDetail)
                    .nextExecutionTime(cron.getNextFireTime().toString())
                    .build());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}
