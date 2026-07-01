package com.prjmng.shared.DTOs.boards;

import com.prjmng.entities.Project;
import com.prjmng.entities.enums.BoardType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBoardRequest {
    @NotNull
    private UUID projectId;
    @NotNull
    @Length(max=100)
    private String name;
    @NotNull
    private BoardType type;
}
