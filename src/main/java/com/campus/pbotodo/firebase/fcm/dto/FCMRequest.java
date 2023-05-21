package com.campus.pbotodo.firebase.fcm.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class FCMRequest extends FCMRequestBase {

    public FCMRequest(String title, String message, Map<String, String> data, String token) {
        super(title, message, data, null);
        this.token = token;
    }

    public FCMRequest(String title, String message, Map<String, String> data, String token, NotificationConfig config) {
        super(title, message, data, config);
        this.token = token;
    }

    private String token;

}