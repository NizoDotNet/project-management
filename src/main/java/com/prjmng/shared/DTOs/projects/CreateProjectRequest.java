package com.prjmng.shared.DTOs.projects;

import com.prjmng.entities.Organization;
import com.prjmng.entities.User;
import com.prjmng.entities.enums.ProjectStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class CreateProjectRequest {
    @NotNull
    private UUID orgId;
    @NotNull
    @Length(max=100)
    private String name;
    private String description;
    private LocalDate startDate;
}

