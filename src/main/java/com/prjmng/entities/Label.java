package com.prjmng.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "labels", schema = "project_management")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Label extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;
}

