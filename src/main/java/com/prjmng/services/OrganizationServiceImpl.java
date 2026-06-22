package com.prjmng.services;

import com.prjmng.entities.Organization;
import com.prjmng.entities.User;
import com.prjmng.repositories.OrganizationRepository;
import com.prjmng.shared.DTOs.organization.CreateOrganizationRequest;
import com.prjmng.shared.DTOs.users.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl {
    private final OrganizationRepository organizationRepository;
    private final UserServiceImpl userService;

    public void createOrganization(CreateOrganizationRequest createOrganizationRequest, Jwt jwt) {
        User userResponse = userService.getOrCreateUser(jwt);

        Organization organization = Organization
                .builder()
                .name(createOrganizationRequest.getName())
                .slug(generateSlug(createOrganizationRequest.getName()))
                .ownerId(userResponse.getId())
                .build();

        organizationRepository.save(organization);
    }

    private String generateSlug(String organizationName) {
        String slug = organizationName
                .toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");

        if(organizationRepository.existsBySlug(slug)) {
            slug += String.format("-$s", UUID.randomUUID().toString());
        }

        return slug;
    }
}
