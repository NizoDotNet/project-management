package com.prjmng.services;

import com.prjmng.entities.Organization;
import com.prjmng.entities.User;
import com.prjmng.repositories.OrganizationRepository;
import com.prjmng.services.specifications.OrganizationSpecifications;
import com.prjmng.shared.DTOs.organization.CreateOrganizationRequest;
import com.prjmng.shared.DTOs.organization.OrganizationResponse;
import com.prjmng.shared.DTOs.organization.UpdateOrganizationRequest;
import com.prjmng.shared.DTOs.users.UserResponse;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final UserService userService;

    public OrganizationResponse createOrganization(CreateOrganizationRequest createOrganizationRequest, Jwt jwt) {
        User user = userService.getOrCreateUser(jwt);

        Organization organization = Organization
                .builder()
                .name(createOrganizationRequest.getName())
                .slug(generateSlug(createOrganizationRequest.getName()))
                .ownerId(user.getId())
                .build();

        organization = organizationRepository.save(organization);

        OrganizationResponse response = mapToOrganizationResponse(organization, user);

        return response;
    }

    public OrganizationResponse updateOrganization(UUID organizationId, UpdateOrganizationRequest organizationRequest, Jwt jwt) {
        User user = userService.getOrCreateUser(jwt);

        Organization organization = organizationRepository.findByIdAndOwnerId(organizationId, user.getId())
                .orElseThrow(() -> new NotFoundException(
                                String.format("Organization with %s id was not found", organizationId)
                        )
                );

        organization.setName(organizationRequest.getName());
        organization.setSlug(generateSlug(organizationRequest.getName()));
        organization = organizationRepository.save(organization);

        OrganizationResponse response = mapToOrganizationResponse(organization, user);

        return response;
    }

    public OrganizationResponse getOrganization(UUID id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                    String.format("Organization with %s id was not found", id)
                )
            );

        OrganizationResponse response = mapToOrganizationResponse(organization, organization.getOwner());
        return response;
    }


    public OrganizationResponse getOrganization(String slug) {
        Organization organization = organizationRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException(
                                String.format("Organization with %s slug was not found", slug)
                        )
                );

        OrganizationResponse response = mapToOrganizationResponse(organization, organization.getOwner());
        return response;
    }



    public Page<OrganizationResponse> getOrganizationsWithPagination(String slug, UUID ownerId, Pageable pageable) {
        Specification<Organization> specification = OrganizationSpecifications.hasSlug(slug)
                .and(OrganizationSpecifications.hasOwner(ownerId));
        var organizations = organizationRepository.findAll(specification, pageable);

        return organizations.map(organization -> mapToOrganizationResponse(organization, organization.getOwner()));
    }


    private String generateSlug(String organizationName) {
        String slug = organizationName
                .toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");

        if(organizationRepository.existsBySlug(slug)) {
            slug += "-" + UUID.randomUUID().toString().substring(0, 8);
        }

        return slug;
    }

    public void delete(UUID id, Jwt jwt) {
        User user = userService.getOrCreateUser(jwt);

        Organization organization = organizationRepository.findByIdAndOwnerId(id, user.getId())
                .orElseThrow(() -> new NotFoundException(
                                String.format("Organization with %s id was not found", id)
                        )
                );

        organizationRepository.delete(organization);
    }

    private static @NonNull OrganizationResponse mapToOrganizationResponse(Organization organization, User owner) {
        return new OrganizationResponse(
                organization.getId(),
                organization.getName(),
                organization.getSlug(),
                new UserResponse(owner.getId(), owner.getKeycloakId(), owner.getEmail(), owner.getFirstName(), owner.getLastName()),
                owner.getId()
        );
    }
}
