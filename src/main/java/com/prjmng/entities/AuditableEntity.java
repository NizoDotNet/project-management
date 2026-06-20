package com.prjmng.entities;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
public abstract class AuditableEntity {
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

}
