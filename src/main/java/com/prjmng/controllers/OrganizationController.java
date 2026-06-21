package com.prjmng.controllers;

import com.prjmng.services.OrganizationServiceImpl;
import com.prjmng.shared.DTOs.organization.CreateOrganizationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationServiceImpl organizationService;

    @PostMapping
    public ResponseEntity<?> createOrganization(@Valid CreateOrganizationRequest request, @AuthenticationPrincipal Jwt jwt) {
        organizationService.createOrganization(request, jwt);

        return ResponseEntity
                .ok()
                .build();
    }
}

