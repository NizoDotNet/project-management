package com.prjmng.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "ticket_comments", schema = "project_management")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketComment extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")                // self-reference — nullable for top-level comments
    private TicketComment parent;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

}

