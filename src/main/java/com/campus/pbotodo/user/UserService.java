package com.campus.pbotodo.user;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.campus.pbotodo.exception.BadRequestException;
import com.campus.pbotodo.security.MyUserDetailService;
import com.campus.pbotodo.security.utils.JwtUtilities;

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

    public UserAuthResponse signup(User userSignupRequest) {
        try {
            String username = userSignupRequest.getUsername();
            String password = userSignupRequest.getPassword();

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
                    .expiredDate(expiredDate)
                    .username(username)
                    .createdBy(username)
                    .updatedBy(username)
                    .build());

            return new UserAuthResponse(username, token);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password");
        }
    }

    public UserAuthResponse signin(User userSigninRequest) {
        try {
            String username = userSigninRequest.getUsername();
            UserDetails userDetails = myUserDetailService.loadUserByUsername(username);
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, userSigninRequest.getPassword()));
            String token = jwtUtilities.generateToken(userDetails);
            Date expiredDate = jwtUtilities.extractExpiration(token);

            userTokenRepo.save(UserToken.builder()
                    .token(token)
                    .expiredDate(expiredDate)
                    .username(username)
                    .createdBy(username)
                    .updatedBy(username)
                    .build());

            return new UserAuthResponse(username, token);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password");
        }
    }

    public UserAuthResponse logout(String bearer) {
        try {
            String jwtToken = jwtUtilities.extractBearer(bearer);
            if (Objects.isNull(jwtToken)) {
                throw new BadCredentialsException("Invalid token");
            }
            String username = jwtUtilities.extractUser(jwtToken);

            UserToken userTokenData = userTokenRepo.findByUsernameAndToken(username, jwtToken);
            userTokenData.setIsActive(false);
            userTokenData.setUpdatedBy(username);
            userTokenData.setUpdatedAt(LocalDateTime.now());
            userTokenRepo.save(userTokenData);

            return new UserAuthResponse(userTokenData.getUsername(), userTokenData.getToken());
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password");
        }
    }

}
