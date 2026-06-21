package com.prjmng.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@AllArgsConstructor
@Entity
@Table(name = "users", schema = "project_management")
public class User extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String keycloakId;
    @Email
    private String email;
    @Length(min=2, max=20)
    private String firstName;
    @Length(min=2, max=20)
    private String lastName;

    private void setId() {}
}
