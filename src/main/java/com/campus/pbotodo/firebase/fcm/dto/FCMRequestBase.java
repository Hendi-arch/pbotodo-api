package com.campus.pbotodo.firebase.fcm.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FCMRequestBase {
    
    private String title;
    
    private String message;
    
    private Map<String, String> data;

    private NotificationConfig config;

}
