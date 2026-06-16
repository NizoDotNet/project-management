package com.prjmng.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name="team_members", schema = "project_management")
public class TeamMember {
    @Id
    private UUID id;

    @ManyToOne(targetEntity = Team.class)
    private UUID teamId;

    @ManyToOne(targetEntity = User.class)
    private UUID userId;
}
