package com.prjmng.shared.DTOs.boards;

import com.prjmng.entities.enums.BoardType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class BoardWithColumnsResponse {
    private UUID id;
    private UUID projectId;
    private String name;
    private List<BoardColumnResponse> columns;
    private BoardType type;
}
