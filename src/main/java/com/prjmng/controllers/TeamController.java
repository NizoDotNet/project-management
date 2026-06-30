package com.prjmng.controllers;

import com.prjmng.entities.User;
import com.prjmng.services.TeamService;
import com.prjmng.services.UserService;
import com.prjmng.shared.DTOs.teams.CreateTeamMemberRequest;
import com.prjmng.shared.DTOs.teams.CreateTeamRequest;
import com.prjmng.shared.DTOs.teams.TeamMemberResponse;
import com.prjmng.shared.DTOs.teams.TeamResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<TeamResponse> create(@Valid CreateTeamRequest request, @AuthenticationPrincipal Jwt jwt) {
        User user = userService.getOrCreateUser(jwt);
        TeamResponse teamResponse = teamService.createTeam(request, user.getId());
        return ResponseEntity.ok(teamResponse);
    }

    @DeleteMapping("{id}")
    public void delete(UUID id, @AuthenticationPrincipal Jwt jwt) {
        User user = userService.getOrCreateUser(jwt);
        teamService.deleteTeam(id, user.getId());
    }

    @GetMapping
    public ResponseEntity<Page<TeamResponse>> getAll(
            @RequestParam(defaultValue = "0", required = true) int page,
            @RequestParam(defaultValue = "10", required = true) int pageSize,
            @RequestParam(required = false) UUID orgId,
            @RequestParam(required = false) String name,
            @AuthenticationPrincipal Jwt jwt) {
        User user = userService.getOrCreateUser(jwt);
        Page<TeamResponse> teams = teamService.getAllWithPagination(PageRequest.of(page, pageSize), user.getId(), orgId, name);
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamResponse> getById(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt) {
        User user = userService.getOrCreateUser(jwt);
        TeamResponse team = teamService.getById(id, user.getId());
        return ResponseEntity.ok(team);
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<List<TeamMemberResponse>> getMembers(@PathVariable UUID id) {
        List<TeamMemberResponse> response = teamService.getTeamMembers(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<TeamMemberResponse> addMember(@PathVariable UUID id, @Valid CreateTeamMemberRequest request, @AuthenticationPrincipal Jwt jwt) {
        User user = userService.getOrCreateUser(jwt);

        TeamMemberResponse response = teamService.addMember(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{teamId}/members/{memberId}")
    public void removeMember(@PathVariable UUID teamId, @PathVariable UUID memberId, @AuthenticationPrincipal Jwt jwt) {
        User user = userService.getOrCreateUser(jwt);
        teamService.removeMember(teamId, memberId, user.getId());
    }
}

