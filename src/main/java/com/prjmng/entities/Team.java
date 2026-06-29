package com.prjmng.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="teams", schema = "project_management")
public class Team extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="org_id",insertable = false, updatable = false)
    private Organization organization;

    @Column(name="org_id")
    private UUID orgId;

    @Length(min=2, max=30)
    private String name;

    @OneToMany(
        mappedBy = "team",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<TeamMember> members = new ArrayList<>();

    public void addMember(TeamMember member) {
        this.members.add(member);
        member.setTeam(this);
    }
}
