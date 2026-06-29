package com.prjmng.services;

import com.prjmng.entities.Organization;
import com.prjmng.entities.Team;
import com.prjmng.entities.TeamMember;
import com.prjmng.entities.User;
import com.prjmng.entities.enums.TeamMemberRole;
import com.prjmng.repositories.OrganizationRepository;
import com.prjmng.repositories.TeamRepository;
import com.prjmng.shared.DTOs.organization.OrganizationResponse;
import com.prjmng.shared.DTOs.teams.CreateTeamRequest;
import com.prjmng.shared.DTOs.teams.TeamResponse;
import com.prjmng.shared.DTOs.users.UserResponse;
import jakarta.validation.Valid;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final OrganizationRepository organizationRepository;
    private final UserService userService;

    public TeamService(TeamRepository teamRepository, OrganizationRepository organizationRepository, UserService userService) {
        this.teamRepository = teamRepository;
        this.organizationRepository = organizationRepository;
        this.userService = userService;
    }

    public TeamResponse createTeam(@Valid CreateTeamRequest createTeamRequest, Jwt jwt) {
        User user = userService.getOrCreateUser(jwt);

        Organization organization = organizationRepository.findByIdAndOwnerId(createTeamRequest.getOrgId(), user.getId())
                .orElseThrow(() -> new RuntimeException("User is not owner and cannot create team in this organization"));

        Team team = Team.builder()
                .orgId(createTeamRequest.getOrgId())
                .name(createTeamRequest.getName())
                .build();

        TeamMember member = TeamMember
                .builder()
                .userId(user.getId())
                .Role(TeamMemberRole.OWNER)
                .build();
        team.addMember(member);

        team = teamRepository.save(team);

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
