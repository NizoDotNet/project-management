package com.prjmng.entities;

import com.prjmng.entities.enums.TeamMemberRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="team_members", schema = "project_management")
public class TeamMember {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",insertable = false, updatable = false)
    private User user;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private TeamMemberRole role;
}
