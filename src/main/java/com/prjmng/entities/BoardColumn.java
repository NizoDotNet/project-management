package com.prjmng.entities;

import com.prjmng.entities.enums.SprintStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "board_columns", schema = "project_management")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardColumn extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer position;
}

