package com.campus.pbotodo.scheduler.implementation.actions.pushnotification;

import com.campus.pbotodo.firebase.fcm.FCMService;
import com.campus.pbotodo.scheduler.implementation.actions.TaskActionInterface;
import com.campus.pbotodo.security.utils.JwtUtilities;
import com.campus.pbotodo.task.TaskRepo;
import com.campus.pbotodo.user.UserTokenRepo;

public class FactoryFcmAction {

    private FactoryFcmAction() {
    }

    public static TaskActionInterface getTaskReminderScheduleAction(FCMService fcmService, UserTokenRepo userTokenRepo,
            JwtUtilities jwtUtilities, TaskRepo taskRepo) {
        return new TaskReminderJobDefinition(fcmService, userTokenRepo, jwtUtilities, taskRepo);
    }

}
