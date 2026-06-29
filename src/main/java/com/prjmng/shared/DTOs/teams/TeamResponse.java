package com.prjmng.shared.DTOs.teams;

import com.prjmng.shared.DTOs.organization.OrganizationResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamResponse {
    private UUID id;
    private OrganizationResponse organization;
    private UUID orgId;
    private String name;
}
