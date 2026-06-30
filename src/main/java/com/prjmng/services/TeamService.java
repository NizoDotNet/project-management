package com.prjmng.services;

import com.prjmng.entities.Organization;
import com.prjmng.entities.Team;
import com.prjmng.entities.TeamMember;
import com.prjmng.entities.User;
import com.prjmng.entities.enums.TeamMemberRole;
import com.prjmng.repositories.OrganizationRepository;
import com.prjmng.repositories.TeamMemberRepository;
import com.prjmng.repositories.TeamRepository;
import com.prjmng.repositories.UserRepository;
import com.prjmng.services.specifications.TeamSpecifications;
import com.prjmng.shared.DTOs.organization.OrganizationResponse;
import com.prjmng.shared.DTOs.teams.CreateTeamMemberRequest;
import com.prjmng.shared.DTOs.teams.CreateTeamRequest;
import com.prjmng.shared.DTOs.teams.TeamMemberResponse;
import com.prjmng.shared.DTOs.teams.TeamResponse;
import com.prjmng.shared.DTOs.users.UserResponse;
import jakarta.validation.Valid;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;

    public TeamResponse createTeam(@Valid CreateTeamRequest createTeamRequest, UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with this " + userId + " id was not foud"));

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

    public void deleteTeam(UUID teamId, UUID userId) {
        boolean isUserOwner = teamMemberRepository.existsByTeamIdAndUserIdAndRole(teamId, userId, TeamMemberRole.OWNER);
        if(!isUserOwner) {
            throw new RuntimeException("Non owner cannot delete team");
        }

        teamRepository.deleteById(teamId);
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

        Team team = teamRepository.findById(id).orElseThrow(() -> new NotFoundException("Team with " + id + " id was not found"));
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new NotFoundException("User with " + request.getUserId() + " id was not found"));

        TeamMember member = TeamMember
                .builder()
                .userId(request.getUserId())
                .team(team)
                .Role(request.getRole())
                .build();

        member = teamMemberRepository.save(member);

        return mapToResponse(member, user);
    }


    public void removeMember(UUID teamId, UUID memberId, UUID userId) {
        if (!teamRepository.existsById(teamId))
            throw new NotFoundException("Team with " + teamId + " id was not found");

        boolean isUserJustMember = teamMemberRepository.existsByTeamIdAndUserIdAndRole(teamId, userId, TeamMemberRole.MEMBER);
        if(isUserJustMember) {
            throw new RuntimeException("Member role cannot remove team-member from team");
        }
        TeamMember member = teamMemberRepository.findById(memberId).orElseThrow(() -> new NotFoundException("Team member with " + memberId + " id was not found"));
        teamMemberRepository.delete(member);
    }

    public List<TeamMemberResponse> getTeamMembers(UUID teamId) {
        return teamMemberRepository.findAllByTeamId(teamId).stream()
                .map(c -> mapToResponse(c, c.getUser()))
                .toList();
    }

    private static @NonNull TeamMemberResponse mapToResponse(TeamMember member, User user) {
        return new TeamMemberResponse(member.getId(), new UserResponse(
                user.getId(),
                user.getKeycloakId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName()),
                member.getRole());
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
