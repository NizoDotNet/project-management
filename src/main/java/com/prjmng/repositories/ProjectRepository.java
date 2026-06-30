package com.prjmng.repositories;

import com.prjmng.entities.Organization;
import com.prjmng.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends  JpaRepository<Project, UUID> {
    Optional<Project> findByIdAndOwnerId(UUID id, UUID ownerId);
    boolean existsByNameAndOrganizationId(String name, UUID organizationId);
}



