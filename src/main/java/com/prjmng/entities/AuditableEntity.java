package com.prjmng.entities;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class AuditableEntity {
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

}
