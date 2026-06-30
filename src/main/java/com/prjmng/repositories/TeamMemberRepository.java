package com.prjmng.repositories;

import com.prjmng.entities.Team;
import com.prjmng.entities.TeamMember;
import com.prjmng.entities.TicketLabel;
import com.prjmng.entities.enums.TeamMemberRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeamMemberRepository extends  JpaRepository<TeamMember, UUID> {
    boolean existsByUserIdAndTeamId(UUID userId, UUID teamId);
    @EntityGraph(attributePaths = {"user"})
    List<TeamMember> findAllByTeamId(UUID teamId);
    boolean existsByTeamIdAndUserIdAndRole(UUID teamId, UUID userId, TeamMemberRole Role);
    @Query("""
    SELECT tm.team FROM TeamMember tm
    WHERE tm.user.id = :userId
    AND (:name IS NULL OR LOWER(tm.team.name) LIKE LOWER(CONCAT('%', :name, '%')))
    AND (:orgId IS NULL OR tm.team.organization.id = :orgId)
    """)
    Page<Team> findTeamsByUserId(
            @Param("userId") UUID userId,
            @Param("name") String name,
            @Param("orgId") UUID orgId,
            Pageable pageable
    );

    Optional<TeamMember> findTeamByUserIdAndTeamId(UUID userId, UUID teamId);
}


