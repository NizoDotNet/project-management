package com.prjmng.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "organizations", schema = "project_management")
public class Organization extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Length(min=2, max=100)
    private String name;
    private String slug;

    @ManyToOne
    @JoinColumn(name="owner_id", insertable = false, updatable = false)
    private User owner;

    @Column(name="owner_id")
    private UUID ownerId;
    private void setId() {}
}
