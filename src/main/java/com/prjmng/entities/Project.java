package com.prjmng.entities;

import com.prjmng.entities.enums.ProjectMemberRole;
import com.prjmng.entities.enums.ProjectStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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
    private User owner;          // ← the project creator/owner

    @Column(nullable = false)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    private LocalDate startDate;
    private LocalDate endDate;
}

