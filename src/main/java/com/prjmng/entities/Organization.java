package com.prjmng.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Data
@Entity
@Table(name = "organizations", schema = "project_management")
public class Organization extends AuditableEntity {
    @Id
    private UUID id;
    @Length(min=2, max=30)
    private String name;
    private String slug;

    private void setId() {}
}
