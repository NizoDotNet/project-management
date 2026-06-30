package com.prjmng.shared.DTOs.projects;

import com.prjmng.entities.enums.ProjectStatus;
import com.prjmng.shared.DTOs.organization.OrganizationResponse;
import com.prjmng.shared.DTOs.users.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponse {
    private UUID id;

    private OrganizationResponse organization;

    private UserResponse owner;

    private String name;

    private String description;

    private ProjectStatus status;

    private LocalDate startDate;
    private LocalDate endDate;
}
