package com.prjmng.controllers;

import com.prjmng.services.UserService;
import com.prjmng.shared.DTOs.users.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public Page<UserResponse> getAll(int page, int pageSize) {
        return userService.getAllWithPagination(PageRequest.of(page, pageSize));
    }
}
