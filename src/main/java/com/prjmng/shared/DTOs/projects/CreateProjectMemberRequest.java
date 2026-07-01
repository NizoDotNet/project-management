package com.prjmng.shared.DTOs.projects;
import com.prjmng.entities.enums.ProjectMemberRole;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class CreateProjectMemberRequest {
    @NotNull
    private UUID userId;
    @NotNull
    private ProjectMemberRole role;
}


