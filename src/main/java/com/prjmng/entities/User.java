package com.prjmng.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Data
@Entity
@Table(name = "users", schema = "project_management")
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class User extends AuditableEntity {
    @Id
    private UUID id;
    private String keycloak_id;
    @Email
    private String email;
    @Length(min=2, max=20)
    private String firstName;
    @Length(min=2, max=20)
    private String lastName;

    private void setId() {}
}
