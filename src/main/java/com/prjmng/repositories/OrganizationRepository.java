package com.prjmng.repositories;

import com.prjmng.entities.NotificationSetting;
import com.prjmng.entities.Organization;
import com.prjmng.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrganizationRepository extends  JpaRepository<Organization, UUID> {
    boolean existsBySlug(String slug);
}


