package com.campus.pbotodo.user;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.campus.pbotodo.exception.BadRequestException;
import com.campus.pbotodo.security.MyUserDetailService;
import com.campus.pbotodo.security.utils.JwtUtilities;
import com.campus.pbotodo.user.dto.UserAuthDto;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserTokenRepo userTokenRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtilities jwtUtilities;

    @Autowired
    private MyUserDetailService myUserDetailService;

    public UserAuthDto signup(User userSignupRequest) {
        try {
            String username = userSignupRequest.getUsername();
            String password = userSignupRequest.getPassword();
            String deviceId = userSignupRequest.getDeviceId();

            User user = userRepo.findByUsername(username);
            if (user != null) {
                throw new BadRequestException("Username is already exist");
            }

            userRepo.save(User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .createdBy(username)
                    .updatedBy(username)
                    .build());

            UserDetails userDetails = myUserDetailService.loadUserByUsername(username);
            String token = jwtUtilities.generateToken(userDetails);
            Date expiredDate = jwtUtilities.extractExpiration(token);

            userTokenRepo.save(UserToken.builder()
                    .token(token)
                    .deviceId(deviceId)
                    .expiredDate(expiredDate)
                    .username(username)
                    .createdBy(username)
                    .updatedBy(username)
                    .build());

            return new UserAuthDto(username, token);
        } catch (AuthenticationException e) {
            throw new BadRequestException("Invalid username/password");
        }
    }

    public UserAuthDto signin(User userSigninRequest) {
        try {
            String username = userSigninRequest.getUsername();
            String deviceId = userSigninRequest.getDeviceId();

            UserDetails userDetails = myUserDetailService.loadUserByUsername(username);
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, userSigninRequest.getPassword()));
            String token = jwtUtilities.generateToken(userDetails);
            Date expiredDate = jwtUtilities.extractExpiration(token);

            userTokenRepo.save(UserToken.builder()
                    .token(token)
                    .deviceId(deviceId)
                    .expiredDate(expiredDate)
                    .username(username)
                    .createdBy(username)
                    .updatedBy(username)
                    .build());

            return new UserAuthDto(username, token);
        } catch (AuthenticationException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public UserAuthDto logout(String bearer) {
        try {
            String jwtToken = jwtUtilities.extractBearer(bearer);
            if (Objects.isNull(jwtToken)) {
                throw new BadRequestException("Invalid token");
            }
            String username = jwtUtilities.extractUser(jwtToken);

            UserToken userTokenData = userTokenRepo.findByUsernameAndToken(username, jwtToken);
            userTokenData.setIsActive(false);
            userTokenData.setUpdatedBy(username);
            userTokenData.setUpdatedAt(LocalDateTime.now());
            userTokenRepo.save(userTokenData);

            return new UserAuthDto(userTokenData.getUsername(), userTokenData.getToken());
        } catch (AuthenticationException e) {
            throw new BadRequestException("Invalid username/password");
        }
    }

    public void forgotPassword(User userData) {
        String username = userData.getUsername();
        String password = userData.getPassword();
        if (!userRepo.existsByUsername(username)) {
            throw new BadRequestException("Invalid username");
        }

        User existingData = userRepo.findByUsername(username);
        existingData.setPassword(passwordEncoder.encode(password));
        existingData.setUpdatedAt(LocalDateTime.now());
        existingData.setUpdatedBy(username);
        userRepo.save(existingData);
    }

}
