package com.campus.pbotodo.firebase.fcm.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationConfig {

    /**
     * Sets the sound to be played when the device receives the notification.
     *
     * @param sound File name of the sound resource or "default".
     */
    @Builder.Default
    private String sound = "default";

    /**
     * Sets the Android notification channel ID (new in Android O). The app must
     * create a channel
     * with this channel ID before any notification with this channel ID is
     * received. If you
     * don't send this channel ID in the request, or if the channel ID provided has
     * not yet been
     * created by the app, FCM uses the channel ID specified in the app manifest.
     *
     * @param channelId The notification's channel ID.
     */
    private String channelId;

    /**
     * Sets the icon of the Android notification.
     *
     * @param icon Icon resource for the notification.
     */
    private String icon;

    /**
     * Sets the URL of the image that is going to be displayed in the notification.
     *
     * @param imageUrl URL of the image that is going to be displayed in the
     *                 notification.
     */
    private String imageUrl;

}
