package com.prjmng.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public abstract class AuditableEntity {
    public Date createdAt;
    public Date updatedAt;
}
