package com.user.controller;

import com.user.config.JwtUtil;
import com.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("/api/auth/users")
public class AuthController {
    @Autowired
    UserService userService;
    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> req) {

        String orgId = req.get("orgId");
        String userId = req.get("user_id");
        String password = req.get("password");

        boolean valid = userService.validateUserLogin(orgId, userId, password);

        if (!valid) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        String token = jwtUtil.generateToken(orgId, userId);

        return ResponseEntity.ok(Map.of(
                "status", true,
                "token", token,
                "userId", userId,
                "orgId", orgId
        ));
    }


}
