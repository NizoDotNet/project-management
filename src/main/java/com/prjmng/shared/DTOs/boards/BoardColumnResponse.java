package com.prjmng.shared.DTOs.boards;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class BoardColumnResponse {
    private UUID id;
    private String name;
    private Integer position;
}
