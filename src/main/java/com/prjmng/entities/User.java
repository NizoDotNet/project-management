package com.prjmng.entities;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.UUID)
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
