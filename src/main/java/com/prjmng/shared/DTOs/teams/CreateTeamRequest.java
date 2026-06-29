package com.prjmng.shared.DTOs.teams;

import jakarta.persistence.Column;
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
public class CreateTeamRequest {
    @Column(name="org_id")
    private UUID orgId;

    @Length(min=2, max=30)
    private String name;
}
