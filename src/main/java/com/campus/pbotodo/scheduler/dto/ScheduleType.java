package com.campus.pbotodo.scheduler.dto;

public enum ScheduleType {

    AT_TIME("at_time_notification_channel_id"),

    FIVE_MINUTES_BEFORE("five_minutes_before_notification_channel_id"),

    TEN_MINUTES_BEFORE("ten_minutes_before_notification_channel_id"),

    FIFTEEN_MINUTES_BEFORE("fifteen_minutes_before_notification_channel_id"),

    THIRTY_MINUTES_BEFORE("thirty_minutes_before_notification_channel_id"),

    ONE_HOUR_BEFORE("one_hour_before_notification_channel_id"),

    TWO_HOURS_BEFORE("two_hours_before_notification_channel_id"),

    ONE_DAY_BEFORE("one_day_before_notification_channel_id"),

    TWO_DAYS_BEFORE("two_days_before_notification_channel_id"),

    UNKNOWN("");

    /**
     * The notification's channel ID.
     */
    public final String notificationChannelId;

    private ScheduleType(String notificationChannelId) {
        this.notificationChannelId = notificationChannelId;
    }

    /**
     *
     * @return the value of the state
     */
    public static ScheduleType fromValue(String value) {
        for (ScheduleType scheduleType : ScheduleType.values()) {
            if (scheduleType.notificationChannelId.equals(value)) {
                return scheduleType;
            }
        }

        return ScheduleType.UNKNOWN;
    }

}
