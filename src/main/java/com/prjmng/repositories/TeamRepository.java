package com.prjmng.repositories;

import com.prjmng.entities.Sprint;
import com.prjmng.entities.Team;
import com.prjmng.entities.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TeamRepository extends  JpaRepository<Team, UUID> {

}

