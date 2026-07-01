package com.prjmng.shared.DTOs.projects;

import com.prjmng.entities.enums.ProjectStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateProjectStatusRequest {
    private ProjectStatus status;
}

