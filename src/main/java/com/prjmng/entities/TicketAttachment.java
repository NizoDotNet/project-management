package com.prjmng.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "ticket_attachments", schema = "project_management")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketAttachment extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by", nullable = false)
    private User uploadedBy;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

}

