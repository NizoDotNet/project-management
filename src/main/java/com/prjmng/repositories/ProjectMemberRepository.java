package com.prjmng.repositories;

import com.prjmng.entities.Project;
import com.prjmng.entities.ProjectMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, UUID> {
    @EntityGraph(attributePaths = {"project.organization", "project.owner"})
    @Query("SELECT pm.project FROM ProjectMember pm WHERE pm.user.id = :userId AND pm.project.organization.id = :orgId")
    Page<Project> findAllProjectsByUserIdAndOrganizationId(@Param("userId") UUID userId, @Param("orgId") UUID orgId, Pageable pageable);

    @EntityGraph(attributePaths = {"project", "project.owner", "project.organization"})
    Optional<ProjectMember> findByProjectIdAndUserId(UUID projectId, UUID userId);
}

