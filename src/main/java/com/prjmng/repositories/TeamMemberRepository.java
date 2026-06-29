package com.prjmng.repositories;

import com.prjmng.entities.Team;
import com.prjmng.entities.TeamMember;
import com.prjmng.entities.TicketLabel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TeamMemberRepository extends  JpaRepository<TeamMember, UUID> {
    boolean existsByUserIdAndTeamId(UUID userId, UUID teamId);
    @EntityGraph(attributePaths = {"user"})
    List<TeamMember> findAllByTeamId(UUID teamId);
}


