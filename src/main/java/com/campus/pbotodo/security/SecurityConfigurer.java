package com.campus.pbotodo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.campus.pbotodo.security.filter.CorsSecurityConfig;

import static org.springframework.security.config.Customizer.withDefaults;

import com.campus.pbotodo.security.filter.JWTSecurityMethodFilters;

@Configuration
@EnableWebSecurity
public class SecurityConfigurer {

    private static final String[] UNSECURED_ENDPOINTS = {
            "/api/auth/*"
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
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(customizer -> customizer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        customizer -> customizer
                                .antMatchers(UNSECURED_ENDPOINTS).permitAll()
                                .anyRequest().authenticated());

        // Allow cross origin requests
        http.cors(withDefaults());

        // Set user details service and add filters
        http.userDetailsService(myUserDetailService);
        http.addFilterBefore(corsSecurityConfig, ChannelProcessingFilter.class);
        http.addFilterBefore(securityMethodFilters, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
