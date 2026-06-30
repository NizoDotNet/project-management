package com.prjmng.services;

import com.prjmng.entities.Organization;
import com.prjmng.entities.Project;
import com.prjmng.entities.ProjectMember;
import com.prjmng.entities.User;
import com.prjmng.entities.enums.ProjectMemberRole;
import com.prjmng.entities.enums.ProjectStatus;
import com.prjmng.repositories.OrganizationRepository;
import com.prjmng.repositories.ProjectRepository;
import com.prjmng.repositories.UserRepository;
import com.prjmng.shared.DTOs.organization.OrganizationResponse;
import com.prjmng.shared.DTOs.projects.CreateProjectRequest;
import com.prjmng.shared.DTOs.projects.ProjectResponse;
import com.prjmng.shared.DTOs.users.UserResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;

    public ProjectResponse createProject(@Valid CreateProjectRequest createProjectRequest, UUID userId) {
        Organization organization = organizationRepository.findByIdAndOwnerId(createProjectRequest.getOrgId(), userId)
                .orElseThrow(() -> new EntityNotFoundException(
                                String.format("Organization with %s id was not found", createProjectRequest.getOrgId())
                        )
                );
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                                String.format("User with %s id was not found", userId)
                        )
                );;

        boolean isNameUsedForProjectInSameOrganization = projectRepository.existsByNameAndOrganizationId(createProjectRequest.getName(), createProjectRequest.getOrgId());

        if(isNameUsedForProjectInSameOrganization) {
            throw new RuntimeException("Project with name " + createProjectRequest.getName() + " already exists");
        }

        Project project = Project
                .builder()
                .owner(user)
                .organization(organization)
                .startDate(createProjectRequest.getStartDate())
                .name(createProjectRequest.getName())
                .description(createProjectRequest.getDescription())
                .status(ProjectStatus.PLANNING)
                .build();

        ProjectMember projectMember = ProjectMember
                .builder()
                .user(user)
                .role(ProjectMemberRole.OWNER)
                .build();

        project.addMember(projectMember);
        project = projectRepository.save(project);

        return mapToResponse(project);
    }

    public void deleteProject(UUID id, UUID userId) {
        Project project = projectRepository.findByIdAndOwnerId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                                String.format("Project with %s id was not found", id)
                        )
                );
        projectRepository.delete(project);

    }

    private static @NonNull ProjectResponse mapToResponse(Project project) {
        UserResponse userResponse = new UserResponse(
                project.getOwner().getId(),
                project.getOwner().getKeycloakId(),
                project.getOwner().getEmail(),
                project.getOwner().getFirstName(),
                project.getOwner().getLastName());

        OrganizationResponse organizationResponse = new OrganizationResponse(
                project.getOrganization().getId(),
                project.getOrganization().getName(),
                project.getOrganization().getSlug(),
                userResponse,
                userResponse.getId()
        );

        return new ProjectResponse(
                project.getId(),
                organizationResponse,
                userResponse,
                project.getName(),
                project.getDescription(),
                project.getStatus(),
                project.getStartDate(),
                project.getEndDate());
    }

}
