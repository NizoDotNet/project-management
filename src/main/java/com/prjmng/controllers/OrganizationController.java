package com.prjmng.controllers;

import com.prjmng.entities.User;
import com.prjmng.services.OrganizationService;
import com.prjmng.services.UserService;
import com.prjmng.shared.DTOs.organization.CreateOrganizationRequest;
import com.prjmng.shared.DTOs.organization.OrganizationResponse;
import com.prjmng.shared.DTOs.organization.UpdateOrganizationRequest;
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
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService organizationService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<OrganizationResponse> createOrganization(@Valid CreateOrganizationRequest request, @AuthenticationPrincipal Jwt jwt) {
        User user = userService.getOrCreateUser(jwt);
        OrganizationResponse response = organizationService.createOrganization(request, user.getId());

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<OrganizationResponse> updateOrganization(@PathVariable UUID id, @Valid UpdateOrganizationRequest request, @AuthenticationPrincipal Jwt jwt) {
        User user = userService.getOrCreateUser(jwt);
        OrganizationResponse response = organizationService.updateOrganization(id, request, user.getId());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    public void updateOrganization(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt) {
        User user = userService.getOrCreateUser(jwt);
        organizationService.delete(id, user.getId());
    }


    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponse> getOrganizationById(@PathVariable UUID id) {
        OrganizationResponse response = organizationService.getOrganization(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("by-slug/{slug}")
    public ResponseEntity<OrganizationResponse> getOrganizationBySlug(@PathVariable String slug) {
        OrganizationResponse response = organizationService.getOrganization(slug);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<OrganizationResponse>> getOrganizationsWithPagination(@RequestParam(name = "page", required = true, defaultValue = "0") int page,
                                                                                  @RequestParam(name = "pageSize", required = true, defaultValue = "1") int pageSize,
                                                                                  @RequestParam(name = "slug", required = false) String slug,
                                                                                  @RequestParam(name = "ownerId", required = false) UUID ownerId) {
        Page<OrganizationResponse> response = organizationService.getOrganizationsWithPagination(
                slug, ownerId, PageRequest.of(page, pageSize)
        );
        return ResponseEntity.ok(response);
    }
}

