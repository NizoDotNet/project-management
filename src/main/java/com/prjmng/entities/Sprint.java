package com.prjmng.entities;

import com.prjmng.entities.enums.MilestoneStatus;
import com.prjmng.entities.enums.SprintStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "sprints", schema = "project_management")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sprint extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SprintStatus status;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;
}
