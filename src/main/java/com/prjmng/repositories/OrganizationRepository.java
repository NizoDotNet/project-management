package com.prjmng.repositories;

import com.prjmng.entities.NotificationSetting;
import com.prjmng.entities.Organization;
import com.prjmng.entities.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface OrganizationRepository extends  JpaRepository<Organization, UUID>, JpaSpecificationExecutor<Organization> {
    @Override
    @EntityGraph(attributePaths = "owner")
    Page<Organization> findAll(
            Specification<Organization> spec,
            Pageable pageable
    );

    Optional<Organization> findBySlug(String slug);
    Optional<Organization> findByIdAndOwnerId(UUID id, UUID ownerId);
    boolean existsBySlug(String slug);
    boolean existsByIdAndOwnerId(UUID id, UUID ownerId);

}


