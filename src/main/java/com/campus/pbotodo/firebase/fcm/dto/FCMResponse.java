package com.campus.pbotodo.firebase.fcm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_NULL)
public class FCMResponse {

    private int status;

    private String message;

    private String errorCode;

}
