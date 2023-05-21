package com.campus.pbotodo.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAuthResponse {
    
    private String user;

    private String token;

}
