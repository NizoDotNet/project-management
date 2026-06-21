package com.prjmng.repositories;

import com.prjmng.entities.Team;
import com.prjmng.entities.TeamMember;
import com.prjmng.entities.TicketLabel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TeamMemberRepository extends  JpaRepository<TeamMember, UUID> {

}


