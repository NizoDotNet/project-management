package com.prjmng.shared.DTOs.organization;

import com.prjmng.shared.DTOs.users.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class OrganizationResponse {
    private UUID id;
    private String name;
    private String slug;
    private UserResponse owner;
    private UUID ownerId;
}
