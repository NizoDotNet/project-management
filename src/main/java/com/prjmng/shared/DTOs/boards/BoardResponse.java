package com.prjmng.shared.DTOs.boards;

import com.prjmng.entities.enums.BoardType;
import com.prjmng.shared.DTOs.projects.ProjectResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class BoardResponse {
    private UUID id;
    private UUID projectId;
    private String name;
    private BoardType type;
}
