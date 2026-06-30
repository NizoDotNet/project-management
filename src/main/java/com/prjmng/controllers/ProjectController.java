package com.prjmng.controllers;

import com.prjmng.entities.User;
import com.prjmng.services.ProjectService;
import com.prjmng.services.UserService;
import com.prjmng.shared.DTOs.projects.CreateProjectRequest;
import com.prjmng.shared.DTOs.projects.ProjectResponse;
import com.prjmng.shared.DTOs.projects.UpdateProjectStatusRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ProjectResponse> create(
            @Valid @RequestBody CreateProjectRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        User user = userService.getOrCreateUser(jwt);
        ProjectResponse response = projectService.createProject(request, user.getId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt) {

        User user = userService.getOrCreateUser(jwt);
        projectService.deleteProject(id, user.getId());
    }

    @GetMapping
    public ResponseEntity<Page<ProjectResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam UUID orgId,
            @AuthenticationPrincipal Jwt jwt) {

        User user = userService.getOrCreateUser(jwt);

        Page<ProjectResponse> response = projectService.getAllWithPagination(
                PageRequest.of(page, pageSize),
                orgId,
                user.getId());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getById(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt) {

        User user = userService.getOrCreateUser(jwt);
        ProjectResponse response = projectService.getById(id, user.getId());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateProjectStatusRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        User user = userService.getOrCreateUser(jwt);
        projectService.updateStatus(id, user.getId(), request);
        return ResponseEntity.noContent().build();
    }
}