package com.prjmng.entities;

import com.prjmng.entities.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "notifications", schema = "project_management")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")                // nullable — some notifications aren't ticket-related
    private Ticket ticket;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationType type;

    @Column(nullable = false)
    private String message;

    @Column(name = "is_read", nullable = false)
    @Builder.Default
    private Boolean isRead = false;

}

