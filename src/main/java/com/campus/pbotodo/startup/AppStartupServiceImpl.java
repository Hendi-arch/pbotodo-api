package com.campus.pbotodo.startup;

import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.campus.pbotodo.firebase.fcm.FCMService;
import com.campus.pbotodo.scheduler.implementation.actions.pushnotification.FactoryFcmAction;
import com.campus.pbotodo.scheduler.implementation.service.TaskScheduleService;
import com.campus.pbotodo.security.utils.JwtUtilities;
import com.campus.pbotodo.task.TaskRepo;
import com.campus.pbotodo.user.UserTokenRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AppStartupServiceImpl implements AppStartupService {

    @Autowired
    @Qualifier("cachedThreadPool")
    private ExecutorService cacheExecutorService;

    @Autowired
    private TaskScheduleService taskScheduleService;

    @Autowired
    private FCMService fcmService;

    @Autowired
    private UserTokenRepo userTokenRepo;

    @Autowired
    private JwtUtilities jwtUtilities;

    @Autowired
    private TaskRepo taskRepo;

    @Override
    @EventListener(classes = ApplicationReadyEvent.class)
    public void handleStartup(ApplicationReadyEvent event) {
        log.info("Application is ready to start");

        // Restore task scheduler from database
        final Runnable runnableTask = () -> FactoryFcmAction
                .getTaskReminderScheduleAction(fcmService, userTokenRepo, jwtUtilities, taskRepo)
                .restore(taskScheduleService);

        cacheExecutorService.execute(runnableTask);
        // End
    }

}
