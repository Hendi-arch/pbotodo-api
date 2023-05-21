package com.campus.pbotodo.firebase.fcm;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.campus.pbotodo.common.BaseServices;
import com.campus.pbotodo.firebase.fcm.dto.FCMRequest;
import com.campus.pbotodo.firebase.fcm.dto.FCMResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Hendi Noviansyah
 */
@Slf4j
@Service
public class FCMServiceImpl implements FCMService {

    private static final String FCM_URL = "http://firebase-service:4004/firebase/web/menu/notification/data";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public FCMResponse sendNotification(FCMRequest argument) throws JsonProcessingException {
        FCMResponse response = new FCMResponse();

        try {
            final FCMResponse res = restTemplate.postForObject(FCM_URL,
                    argument,
                    FCMResponse.class);

            response = res;
        } catch (HttpClientErrorException.NotFound e) {
            log.error(e.getResponseBodyAsString());
            response = BaseServices.stringJsonToObject(e.getResponseBodyAsString(), FCMResponse.class);
        } catch (HttpClientErrorException.BadRequest e) {
            log.error(e.getResponseBodyAsString());
            response = BaseServices.stringJsonToObject(e.getResponseBodyAsString(), FCMResponse.class);
        } catch (HttpServerErrorException.InternalServerError e) {
            log.error(e.getResponseBodyAsString());
            response = BaseServices.stringJsonToObject(e.getResponseBodyAsString(), FCMResponse.class);
            if (response != null) {
                response.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.name());
            }
        }

        return response;
    }

}
