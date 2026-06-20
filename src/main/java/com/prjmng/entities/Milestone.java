package com.prjmng.entities;

import com.prjmng.entities.enums.MilestoneStatus;
import com.prjmng.entities.enums.TicketPriority;
import com.prjmng.entities.enums.TicketStatus;
import com.prjmng.entities.enums.TicketType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "milestones", schema = "project_management")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Milestone extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MilestoneStatus status;
}
