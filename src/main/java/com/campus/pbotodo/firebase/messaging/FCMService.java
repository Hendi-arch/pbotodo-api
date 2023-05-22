package com.campus.pbotodo.firebase.messaging;

import java.time.Duration;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.ApnsFcmOptions;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.extern.slf4j.Slf4j;
import com.google.firebase.messaging.Notification;

@Slf4j
public class FCMService {

    private FCMService() {
    }

    /**
     * Public method to send push notification
     */
    public static void sendMessage(PushNotificationRequest request) throws FirebaseMessagingException {
        Message message = request.getData() == null ? getPreconfiguredMessageToToken(request)
                : getPreconfiguredMessageWithData(request);
        String response = sendAndGetResponse(message);
        log.info("Sent message to token. Device token: {}, Response: {}, Message: {}", request.getToken(), response,
                messageToJson(message));
    }

    /**
     * Private method to send the message and get the response
     */
    private static String sendAndGetResponse(Message message) throws FirebaseMessagingException {
        return FirebaseMessaging.getInstance().send(message);
    }

    /**
     * Get the Android configuration for push notification
     */
    private static AndroidConfig getAndroidConfig(PushNotificationRequest request) {
        final PushNotificationConfig config = request.getConfig();

        return AndroidConfig.builder()
                .setCollapseKey(request.getTopic())
                .setNotification(AndroidNotification.builder()
                        .setIcon(config.getIcon())
                        .setImage(config.getImageUrl())
                        .setChannelId(config.getChannelId())
                        .setSound(config.getSound())
                        .build())
                .setPriority(AndroidConfig.Priority.HIGH)
                .setTtl(Duration.ofDays(28).toMillis())
                .build();
    }

    /**
     * Get the APNs (Apple Push Notification service) configuration for push
     * notification
     */
    private static ApnsConfig getApnsConfig(PushNotificationRequest request) {
        final PushNotificationConfig config = request.getConfig();

        return ApnsConfig.builder()
                .setAps(Aps.builder()
                        .setCategory(request.getTopic())
                        .setThreadId(request.getTopic())
                        .setSound(config.getSound())
                        .build())
                .setFcmOptions(ApnsFcmOptions.builder()
                        .setImage(config.getImageUrl())
                        .build())
                .build();
    }

    /**
     * Get the preconfigured message to send push notification with only token
     */
    private static Message getPreconfiguredMessageToToken(PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request)
                .setToken(request.getToken())
                .build();
    }

    /**
     * Get the preconfigured message to send push notification with data
     */
    private static Message getPreconfiguredMessageWithData(PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request)
                .putAllData(request.getData())
                .setToken(request.getToken())
                .build();
    }

    /**
     * Get the preconfigured message builder for push notification
     */
    private static Message.Builder getPreconfiguredMessageBuilder(PushNotificationRequest request) {
        final AndroidConfig androidConfig = getAndroidConfig(request);
        final ApnsConfig apnsConfig = getApnsConfig(request);

        return Message.builder()
                .setApnsConfig(apnsConfig)
                .setAndroidConfig(androidConfig)
                .setNotification(Notification.builder()
                        .setTitle(request.getTitle())
                        .setBody(request.getMessage())
                        .build());
    }

    /**
     * Convert the message object to JSON
     */
    private static String messageToJson(Message message) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(message);
    }
}