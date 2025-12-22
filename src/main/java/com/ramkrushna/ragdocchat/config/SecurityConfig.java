package com.ramkrushna.ragdocchat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // disable CSRF for APIs (Postman, REST)
                .csrf(csrf -> csrf.disable())

                // allow these endpoints
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/documents/upload",
                                "/api/search")
                        .permitAll()
                        .anyRequest().authenticated());

        return http.build();
    }
}