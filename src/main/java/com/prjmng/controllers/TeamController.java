package com.prjmng.controllers;

import com.prjmng.services.TeamService;
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

import java.util.UUID;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<TeamResponse> create(@Valid CreateTeamRequest request, @AuthenticationPrincipal Jwt jwt) {
        TeamResponse teamResponse = teamService.createTeam(request, jwt);
        return ResponseEntity.ok(teamResponse);
    }

    @GetMapping
    public ResponseEntity<Page<TeamResponse>> get(
            @RequestParam(defaultValue = "1", required = true) int page,
            @RequestParam(defaultValue = "10", required = true) int pageSize,
            @RequestParam(required = false) UUID orgId,
            @RequestParam(required = false) String name) {
        Page<TeamResponse> teams = teamService.getAllWithPagination(PageRequest.of(page, pageSize), orgId, name);
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamResponse> getById(@PathVariable UUID id) {
        TeamResponse team = teamService.getById(id);
        return ResponseEntity.ok(team);
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<TeamMemberResponse> addMember(@PathVariable UUID id, @Valid CreateTeamMemberRequest request) {
        TeamMemberResponse response = teamService.addMember(id, request);
        return ResponseEntity.ok(response);
    }
}
