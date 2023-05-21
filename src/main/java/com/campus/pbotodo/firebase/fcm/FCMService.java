package com.campus.pbotodo.firebase.fcm;

import com.campus.pbotodo.firebase.fcm.dto.FCMRequest;
import com.campus.pbotodo.firebase.fcm.dto.FCMResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface FCMService {

    FCMResponse sendNotification(FCMRequest argument) throws JsonProcessingException;

}
