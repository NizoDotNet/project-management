package com.prjmng.entities;

import lombok.Data;

import java.util.Date;

@Data
public abstract class AuditableEntity {
    public Date createdAt;
    public Date updatedAt;

}
