package com.prjmng.shared.DTOs.teams;

import com.prjmng.entities.enums.TeamMemberRole;
import com.prjmng.shared.DTOs.users.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamMemberResponse {
    private UUID id;
    private UserResponse user;
    private TeamMemberRole Role;
}
