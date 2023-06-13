package com.campus.pbotodo.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAuthDto {
    
    private String user;

    private String token;

}
