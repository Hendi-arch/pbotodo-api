package com.campus.pbotodo.scheduler.implementation.actions.pushnotification;

import com.campus.pbotodo.scheduler.implementation.actions.TaskActionInterface;
import com.campus.pbotodo.security.utils.JwtUtilities;
import com.campus.pbotodo.task.TaskRepo;
import com.campus.pbotodo.user.UserTokenRepo;

public class FactoryFcmAction {

    private FactoryFcmAction() {
    }

    public static TaskActionInterface getTaskReminderScheduleAction(UserTokenRepo userTokenRepo,
            JwtUtilities jwtUtilities, TaskRepo taskRepo) {
        return new TaskReminderJobDefinition(userTokenRepo, jwtUtilities, taskRepo);
    }

}
