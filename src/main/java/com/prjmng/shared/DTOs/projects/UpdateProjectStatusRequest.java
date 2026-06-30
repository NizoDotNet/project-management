package com.prjmng.shared.DTOs.projects;

import com.prjmng.entities.enums.ProjectStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProjectStatusRequest {
    private ProjectStatus status;
}
