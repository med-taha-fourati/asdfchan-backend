package com.trbinc.asdfchanbackend.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/boards").permitAll()
                .requestMatchers("/api/posts").permitAll()
                .requestMatchers("/api/posts/all").permitAll()
                .requestMatchers("/api/posts/create").permitAll() // add more later
                //.requestMatchers("/api/admin/boards/create").permitAll()
                //.requestMatchers("/api/admin/boards/delete").permitAll()
                //.requestMatchers("/api/admin/**").hasRole("ADMIN") // restrict admin routes
                .anyRequest().authenticated()
        ).httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
