package com.campus.pbotodo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.campus.pbotodo.security.filter.CorsSecurityConfig;

import com.campus.pbotodo.security.filter.JWTSecurityMethodFilters;

@Configuration
@EnableWebSecurity
public class SecurityConfigurer {

    private static final String[] AUTH_WHITELIST = {
            "/api/auth/signin",
            "/api/auth/signup"
    };

    // Define final fields and inject them through constructor
    private final MyUserDetailService myUserDetailService;

    private final JWTSecurityMethodFilters securityMethodFilters;

    private final CorsSecurityConfig corsSecurityConfig;

    public SecurityConfigurer(MyUserDetailService myUserDetailService,
            JWTSecurityMethodFilters securityMethodFilters,
            CorsSecurityConfig corsSecurityConfig) {
        this.myUserDetailService = myUserDetailService;
        this.securityMethodFilters = securityMethodFilters;
        this.corsSecurityConfig = corsSecurityConfig;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Configure security for the application
        http.csrf().disable()
                .exceptionHandling()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeHttpRequests()
                .antMatchers(AUTH_WHITELIST)
                .permitAll().anyRequest().authenticated();

        // Allow cross origin requests
        http.cors();

        // Set user details service and add filters
        http.userDetailsService(myUserDetailService);
        http.addFilterBefore(corsSecurityConfig, ChannelProcessingFilter.class);
        http.addFilterBefore(securityMethodFilters, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
