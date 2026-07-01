package com.prjmng.services;

import com.prjmng.entities.Organization;
import com.prjmng.entities.Project;
import com.prjmng.entities.ProjectMember;
import com.prjmng.entities.User;
import com.prjmng.entities.enums.ProjectMemberRole;
import com.prjmng.entities.enums.ProjectStatus;
import com.prjmng.repositories.OrganizationRepository;
import com.prjmng.repositories.ProjectMemberRepository;
import com.prjmng.repositories.ProjectRepository;
import com.prjmng.repositories.UserRepository;
import com.prjmng.shared.DTOs.organization.OrganizationResponse;
import com.prjmng.shared.DTOs.projects.*;
import com.prjmng.shared.DTOs.users.UserResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
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
        project.setMembers(new ArrayList<>());

        ProjectMember projectMember = ProjectMember
                .builder()
                .user(user)
                .role(ProjectMemberRole.OWNER)
                .build();

        project.addMember(projectMember);
        project = projectRepository.save(project);

        return mapToResponse(project);
    }

    public ProjectMemberResponse addMember(UUID id, @Valid CreateProjectMemberRequest request, UUID userId) {
        Project project = projectRepository.findByIdAndOwnerId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                                String.format("Project with %s id was not found", id)
                        )
                );;
        boolean isUserAlreadyMember = projectMemberRepository.existsByProjectIdAndUserId(id, request.getUserId());
        if(isUserAlreadyMember) {
            throw new RuntimeException("User with id " + request.getUserId() + " is already member");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(
                                String.format("User with %s id was not found", request.getUserId())
                        )
                );

        ProjectMember projectMember = ProjectMember
                .builder()
                .user(user)
                .project(project)
                .role(request.getRole())
                .build();
        projectMember = projectMemberRepository.save(projectMember);
        return mapToResponse(projectMember);
    }

    public void removeMember(UUID projectId, UUID memberId, UUID userId) {
        boolean canUserRemoveMember = projectMemberRepository
                .existsByProjectIdAndUserIdAndRoleIn(projectId, userId, List.of(ProjectMemberRole.OWNER, ProjectMemberRole.MANAGER));
        if(!canUserRemoveMember) {
            throw new RuntimeException("User with id " + userId + " cannot remove member from project with id " + projectId);
        }
        projectMemberRepository.deleteById(memberId);
    }

    public void setEndDateOfProject(UUID projectId, UUID userId, UpdateProjectEndDateRequest request) {
        boolean canUserRemoveMember = projectMemberRepository
                .existsByProjectIdAndUserIdAndRoleIn(projectId, userId, List.of(ProjectMemberRole.OWNER, ProjectMemberRole.MANAGER));
        if(!canUserRemoveMember) {
            throw new RuntimeException("User with id " + userId + " cannot set end date in project with id " + projectId);
        }

        Project project = projectRepository.findByIdAndOwnerId(projectId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                                String.format("Project with %s id was not found", projectId)
                        )
                );

        if(project.getStartDate().isAfter(request.getEndDate())) {
            throw new ValidationException("End date cannot be before start date of project");
        }

        project.setEndDate(request.getEndDate());
        projectRepository.save(project);
    }

    private static @NonNull ProjectMemberResponse mapToResponse(ProjectMember projectMember) {
        return new ProjectMemberResponse(
                projectMember.getId(),
                mapToResponse(projectMember.getProject()),
                new UserResponse(projectMember.getUser().getId(), projectMember.getUser().getKeycloakId(), projectMember.getUser().getEmail(), projectMember.getUser().getFirstName(), projectMember.getUser().getLastName()),
                projectMember.getRole()
        );
    }

    public void deleteProject(UUID id, UUID userId) {
        Project project = projectRepository.findByIdAndOwnerId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                                String.format("Project with %s id was not found", id)
                        )
                );
        projectRepository.delete(project);

    }

    public Page<ProjectResponse> getAllWithPagination(PageRequest pageRequest, UUID orgId, UUID userId) {
        Page<Project> projects = projectMemberRepository.findAllProjectsByUserIdAndOrganizationId(userId, orgId, pageRequest);
        return projects.map(p -> mapToResponse(p));
    }

    public ProjectResponse getById(UUID id, UUID userId) {
        ProjectMember projectMember = projectMemberRepository.findByProjectIdAndUserId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Project member with %s id was not found", id)
                )
        );

        return mapToResponse(projectMember.getProject());
    }

    public void updateStatus(UUID id, UUID userId, UpdateProjectStatusRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                                String.format("Project with %s id was not found", id)
                        )
                );

        if(!project.getOwner().getId().equals(userId)) {
            throw new RuntimeException("User with id " + userId + " is not project owner. He cannot update status");
        }

        project.setStatus(request.getStatus());
        projectRepository.save(project);

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
