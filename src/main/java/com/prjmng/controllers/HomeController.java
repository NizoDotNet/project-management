package com.prjmng.controllers;

import com.prjmng.services.UserServiceImpl;
import com.prjmng.shared.DTOs.users.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("public/api")
@RequiredArgsConstructor
public class HomeController {
    private final UserServiceImpl userService;

    @GetMapping("/")
    public String Ping() {
        return "Pong";
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getUser(@AuthenticationPrincipal Jwt jwt) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assert auth != null;

        UserResponse userResponse = userService.getOrCreateUser(jwt);
        return ResponseEntity.ok(userResponse);
    }


}
