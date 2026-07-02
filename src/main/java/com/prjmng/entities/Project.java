package com.prjmng.entities;

import com.prjmng.entities.enums.ProjectMemberRole;
import com.prjmng.entities.enums.ProjectStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "projects", schema = "project_management")
public class Project extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", nullable = false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    private LocalDate startDate;
    private LocalDate endDate;

    @Builder.Default
    @OneToMany(
        mappedBy = "project",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<ProjectMember> members = new ArrayList<>();


    public void addMember(ProjectMember projectMember) {
        members.add(projectMember);
        projectMember.setProject(this);
    }
}

