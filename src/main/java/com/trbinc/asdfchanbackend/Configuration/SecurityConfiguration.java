package com.trbinc.asdfchanbackend.Configuration;

import com.trbinc.asdfchanbackend.Middleware.JWTUtil;
import com.trbinc.asdfchanbackend.Middleware.JwtRequestFilter;
import com.trbinc.asdfchanbackend.Services.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final int STRENGTH = 10; // BCrypt strength factor

    @Autowired
    private final UserRoleService userRoleService;

    @Autowired
    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfiguration(UserRoleService userRoleService, JwtRequestFilter jwtRequestFilter) {
        this.userRoleService = userRoleService;
        this.jwtRequestFilter = jwtRequestFilter;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/boards").permitAll()
                .requestMatchers("/api/posts").permitAll()
                .requestMatchers("/api/posts/all").permitAll()
                .requestMatchers("/api/posts/create").permitAll() // add more later
                .requestMatchers("/api/posts/delete").hasRole("ADMIN") // add more later
                .requestMatchers("/api/auth/**").permitAll() // allow public access to auth routes
                //.requestMatchers("/api/admin/boards/create").permitAll()
                //.requestMatchers("/api/admin/boards/delete").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN") // restrict admin routes
                .anyRequest().authenticated()
        )//.httpBasic(Customizer.withDefaults());
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(STRENGTH);
    }
}
