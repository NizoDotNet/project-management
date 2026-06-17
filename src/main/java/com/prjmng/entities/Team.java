package com.prjmng.entities;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Entity
@Table(name="teams", schema = "project_management")
public class Team extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name="org_id")
    private Organization organization;
    @Length(min=2, max=30)
    private String name;
}
