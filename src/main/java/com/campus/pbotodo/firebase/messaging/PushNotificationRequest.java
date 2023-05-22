package com.campus.pbotodo.firebase.messaging;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PushNotificationRequest {
    /**
     * Sets the title of the notification.
     *
     * @param title Title of the notification.
     */
    private String title;

    /**
     * Sets the name of the FCM topic to which the message should be sent. Topic
     * names may
     * contain the {@code /topics/} prefix.
     *
     * @param topic A valid topic name.
     */
    private String topic;

    /**
     * Sets the registration token of the device to which the message should be
     * sent.
     *
     * @param token A valid device registration token.
     */
    private String token;

    /**
     * Sets the message of the notification.
     *
     * @param message Body of the notification.
     */
    private String message;

    /**
     * Adds all the key-value pairs in the given map to the message as data fields.
     * None of the
     * keys or values may be null.
     *
     * @param data A non-null map of data fields. Map must not contain null keys or
     *             values.
     */
    private Map<String, String> data;

    /**
     * Sets the Push notification config to be included in the message.
     *
     * @param config An {@link PushNotificationConfig} instance.
     */
    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    private PushNotificationConfig config = new PushNotificationConfig();
}