package com.prjmng.shared.DTOs.boards;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class UpdateBoardColumnRequest {
    @NotNull
    @Length(max=100)
    private String name;
    @NotNull
    private Integer position;
}
