package com.prjmng.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Entity
@Data
@Table(name="teams", schema = "project_management")
public class Team extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="org_id")
    private Organization organization;

    @Column(name="org_id")
    private UUID orgId;

    @Length(min=2, max=30)
    private String name;
}
