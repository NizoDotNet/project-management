package com.prjmng.services;

import com.prjmng.entities.Organization;
import com.prjmng.repositories.OrganizationRepository;
import org.springframework.stereotype.Service;

@Service
public class OrganizationServiceImpl {
    private final OrganizationRepository organizationRepository;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

}
