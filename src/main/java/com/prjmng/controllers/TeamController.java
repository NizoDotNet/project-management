package com.prjmng.controllers;

import com.prjmng.services.TeamService;
import com.prjmng.shared.DTOs.teams.CreateTeamRequest;
import com.prjmng.shared.DTOs.teams.TeamResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
