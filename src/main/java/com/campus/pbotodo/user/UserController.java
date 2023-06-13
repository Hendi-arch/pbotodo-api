package com.campus.pbotodo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.campus.pbotodo.user.dto.UserAuthDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(value = "/signin")
    public ResponseEntity<UserAuthDto> signin(@Valid @RequestBody User userSigninRequest) {
        return new ResponseEntity<>(userService.signin(userSigninRequest), HttpStatus.OK);
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping(value = "/signup")
    public ResponseEntity<UserAuthDto> signup(@Valid @RequestBody User userSignupRequest) {
        return new ResponseEntity<>(userService.signup(userSignupRequest), HttpStatus.CREATED);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(value = "/logout")
    public ResponseEntity<UserAuthDto> logout(@RequestHeader(name = "Authorization") String bearer) {
        return new ResponseEntity<>(userService.logout(bearer), HttpStatus.OK);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @PutMapping(value = "/forgot_password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody User userData) {
        userService.forgotPassword(userData);
        return ResponseEntity.ok(
                "Congratulations! Your password has been successfully reset. You can now log in to your account using your new password.");
    }

}
