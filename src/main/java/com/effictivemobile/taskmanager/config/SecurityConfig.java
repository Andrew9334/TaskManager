package com.effictivemobile.taskmanager.config;

import com.effictivemobile.taskmanager.security.JWTAuthenticationFilter;
import com.effictivemobile.taskmanager.service.JWTTokenProvider;
import com.effictivemobile.taskmanager.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JWTTokenProvider jwtTokenProvider;

    public SecurityConfig(JWTTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(new JWTAuthenticationFilter(jwtTokenProvider), org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .requestMatchers("/api/auth/**").permitAll();
        return http.build();
    }
}