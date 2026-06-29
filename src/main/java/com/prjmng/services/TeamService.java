package com.prjmng.services;

import com.prjmng.entities.Organization;
import com.prjmng.entities.Team;
import com.prjmng.entities.TeamMember;
import com.prjmng.entities.User;
import com.prjmng.entities.enums.TeamMemberRole;
import com.prjmng.repositories.OrganizationRepository;
import com.prjmng.repositories.TeamMemberRepository;
import com.prjmng.repositories.TeamRepository;
import com.prjmng.services.specifications.TeamSpecifications;
import com.prjmng.shared.DTOs.organization.OrganizationResponse;
import com.prjmng.shared.DTOs.teams.CreateTeamMemberRequest;
import com.prjmng.shared.DTOs.teams.CreateTeamRequest;
import com.prjmng.shared.DTOs.teams.TeamMemberResponse;
import com.prjmng.shared.DTOs.teams.TeamResponse;
import com.prjmng.shared.DTOs.users.UserResponse;
import jakarta.validation.Valid;
import jakarta.ws.rs.NotFoundException;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final OrganizationRepository organizationRepository;
    private final UserService userService;

    public TeamService(TeamRepository teamRepository, TeamMemberRepository teamMemberRepository, OrganizationRepository organizationRepository, UserService userService) {
        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.organizationRepository = organizationRepository;
        this.userService = userService;
    }

    public TeamResponse createTeam(@Valid CreateTeamRequest createTeamRequest, Jwt jwt) {
        User user = userService.getOrCreateUser(jwt);

        Organization organization = organizationRepository.findByIdAndOwnerId(createTeamRequest.getOrgId(), user.getId())
                .orElseThrow(() -> new RuntimeException("User is not owner and cannot create team in this organization"));

        Team team = new Team();
        team.setOrgId(createTeamRequest.getOrgId());
        team.setName(createTeamRequest.getName());

        TeamMember member = TeamMember
                .builder()
                .userId(user.getId())
                .Role(TeamMemberRole.OWNER)
                .build();
        team.addMember(member);

        team = teamRepository.save(team);

        return mapToResponse(team, organization);

    }

    public Page<TeamResponse> getAllWithPagination(PageRequest pageRequest, UUID orgId, String name) {
        Specification<Team> specification = TeamSpecifications.hadOrgId(orgId)
                .and(TeamSpecifications.hasName(name));

        Page<Team> teams = teamRepository.findAll(specification, pageRequest);

        return teams.map(t -> mapToResponse(t, t.getOrganization()));
    }

    public TeamResponse getById(UUID id) {
        Team team = teamRepository.findById(id).orElseThrow(() -> new NotFoundException("Team with " + id + " id was not found"));

        return mapToResponse(team, team.getOrganization());
    }

    public TeamMemberResponse addMember(UUID id, @Valid CreateTeamMemberRequest request) {
        boolean isAlreadyMember = teamMemberRepository.existsByUserIdAndTeamId(request.getUserId(), id);
        if(isAlreadyMember) {
            throw new RuntimeException("User already is member");
        }
        TeamMember member = TeamMember
                .builder()
                .userId(request.getUserId())
                .Role(request.getRole())
                .build();
        member = teamMemberRepository.save(member);

        return new TeamMemberResponse(member.getId(), new UserResponse(
                member.getUser().getId(),
                member.getUser().getKeycloakId(),
                member.getUser().getEmail(),
                member.getUser().getFirstName(),
                member.getUser().getLastName()),member.getRole());
    }
    private static @NonNull TeamResponse mapToResponse(Team team, Organization organization) {
        return new TeamResponse(team.getId(),
                new OrganizationResponse(organization.getId(),
                        organization.getName(),
                        organization.getSlug(),
                        new UserResponse(
                                organization.getOwner().getId(),
                                organization.getOwner().getKeycloakId(),
                                organization.getOwner().getEmail(),
                                organization.getOwner().getFirstName(),
                                organization.getOwner().getLastName()),
                        organization.getOwnerId()),
                team.getOrgId(),
                team.getName());
    }

}
