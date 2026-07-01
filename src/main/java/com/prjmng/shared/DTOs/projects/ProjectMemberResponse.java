package com.prjmng.shared.DTOs.projects;

import com.prjmng.entities.Project;
import com.prjmng.entities.User;
import com.prjmng.entities.enums.ProjectMemberRole;
import com.prjmng.shared.DTOs.users.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class ProjectMemberResponse {
    private UUID id;
    private ProjectResponse project;
    private UserResponse user;
    private ProjectMemberRole role;
}
