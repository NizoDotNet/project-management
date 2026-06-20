package com.prjmng.entities;

import com.prjmng.entities.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "ticket_labels", schema = "project_management")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketLabel extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "label_id", nullable = false)
    private Label label;
}

