package com.prjmng.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Entity
@Table(name="teams", schema = "project_management")
public class Team extends AuditableEntity {
    @Id
    private UUID id;

    @ManyToOne(targetEntity = Organization.class)
    private UUID orgId;
    @Length(min=2, max=30)
    private String name;
}
