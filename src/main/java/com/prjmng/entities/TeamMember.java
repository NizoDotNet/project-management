package com.prjmng.entities;

import com.prjmng.entities.enums.TeamMemberRole;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@Table(name="team_members", schema = "project_management")
public class TeamMember {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id",insertable = false, updatable = false)
    private Team team;

    @Column(name = "team_id")
    private UUID teamId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",insertable = false, updatable = false)
    private User user;

    @Column(name = "user_id")
    private UUID userId;

    @Enumerated(EnumType.STRING)
    private TeamMemberRole Role;
}
