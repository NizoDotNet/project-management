package com.prjmng.repositories;

import com.prjmng.entities.Sprint;
import com.prjmng.entities.Team;
import com.prjmng.entities.TeamMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface TeamRepository extends  JpaRepository<Team, UUID>, JpaSpecificationExecutor<Team> {
    @Override
    @EntityGraph(attributePaths = "organization")
    Page<Team> findAll(Specification<Team> specification, Pageable pageable);
}

