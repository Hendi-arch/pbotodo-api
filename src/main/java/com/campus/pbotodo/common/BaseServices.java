package com.campus.pbotodo.common;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.lang.Nullable;

import com.campus.pbotodo.scheduler.dto.ScheduleDefinitionDto;
import com.campus.pbotodo.scheduler.implementation.definition.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseServices {

    private BaseServices() {
    }

    public static LocalDateTime stringToLocalDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime,
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
                .atZone(ZoneId.of("Asia/Jakarta")).toLocalDateTime();
    }

    public static LocalDate stringToLocalDate(String dateTime) {
        return LocalDate.parse(dateTime,
                DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    public static String formatDateToString(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static <T> T stringJsonToObject(String source, Class<T> clazz) throws JsonProcessingException {
        return new ObjectMapper().readValue(source, clazz);
    }

    public static String objectToStringJson(Object value) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(value);
    }

    public static <T> T convertObject(Object source, TypeReference<T> toTypeRef) {
        return new ObjectMapper().convertValue(source, toTypeRef);
    }

    public static String extractBearer(String bearerToken) {
        if (bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        } else {
            return bearerToken;
        }
    }

    @Nullable
    public static IScheduleDefinition getScheduleDefinition(ScheduleDefinitionDto scheduleDefinition) {
        switch (scheduleDefinition.getScheduleType()) {
            case AT_TIME:
                return new ScheduleAtTimeOfEvent(scheduleDefinition.getAlarmTime());
            case FIVE_MINUTES_BEFORE:
                return new Schedule5MinutesBeforeEvent(scheduleDefinition.getAlarmTime());
            case TEN_MINUTES_BEFORE:
                return new Schedule10MinutesBeforeEvent(scheduleDefinition.getAlarmTime());
            case FIFTEEN_MINUTES_BEFORE:
                return new Schedule15MinutesBeforeEvent(scheduleDefinition.getAlarmTime());
            case THIRTY_MINUTES_BEFORE:
                return new Schedule30MinutesBeforeEvent(scheduleDefinition.getAlarmTime());
            case ONE_HOUR_BEFORE:
                return new Schedule1HourBeforeEvent(scheduleDefinition.getAlarmTime());
            case TWO_HOURS_BEFORE:
                return new Schedule2HoursBeforeEvent(scheduleDefinition.getAlarmTime());
            case ONE_DAY_BEFORE:
                return new Schedule1DayBeforeEvent(scheduleDefinition.getAlarmTime());
            case TWO_DAYS_BEFORE:
                return new Schedule2DaysBeforeEvent(scheduleDefinition.getAlarmTime());
            default:
                return null;
        }
    }

    public static String getTaskId(Long id, String username) {
        return "task-id-" + id + "-" + username;
    }

    public static String getReminderTaskId(Long id, String username) {
        return "reminder-task-id-" + id + "-" + username;
    }

}
