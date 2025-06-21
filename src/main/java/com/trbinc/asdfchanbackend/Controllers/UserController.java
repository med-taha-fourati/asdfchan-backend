package com.trbinc.asdfchanbackend.Controllers;

import com.trbinc.asdfchanbackend.Middleware.JWTUtil;
import com.trbinc.asdfchanbackend.Models.User;
import com.trbinc.asdfchanbackend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTUtil jwtUtil;

    @CrossOrigin(origins="*")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                return new ResponseEntity<>("Username already exists", HttpStatus.CONFLICT);
            }
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                return new ResponseEntity<>("Email already exists", HttpStatus.CONFLICT);
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));

            if (user.getRole() == null) {
                user.setRole("USER");
            }

            userRepository.save(user);

            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal server error while registering user" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins="*")
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User userBody) {
        try {
            User user = userRepository.findByUsername(userBody.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!passwordEncoder.matches(userBody.getPassword(), user.getPassword())) {
                return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
            }

            String token = jwtUtil.generateToken(user.getUsername());
            // this token should probably be stored in a localStorage session
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal server error while logging in " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @CrossOrigin(origins="*")
    @PostMapping("/retrieve/{token}")
    public ResponseEntity<?> retrieveCredentials(@PathVariable String token) {
        try {
            System.out.println(token);
            if (jwtUtil.isTokenExpired(token)) {
                return new ResponseEntity<>("Token is expired", HttpStatus.UNAUTHORIZED);
            }

            String username = jwtUtil.extractUsername(token);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            HashMap<String, String> credentials = new HashMap<>();
            credentials.put("username", user.getUsername());
            credentials.put("email", user.getEmail());
            credentials.put("role", user.getRole());

            return new ResponseEntity<>(credentials, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving credentials: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
