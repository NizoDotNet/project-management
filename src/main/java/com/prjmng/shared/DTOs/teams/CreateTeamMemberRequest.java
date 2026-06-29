package com.prjmng.shared.DTOs.teams;

import com.prjmng.entities.enums.TeamMemberRole;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateTeamMemberRequest {
    @NotNull
    private UUID userId;
    private TeamMemberRole role;
}
