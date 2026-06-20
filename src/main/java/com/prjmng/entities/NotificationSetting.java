package com.prjmng.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "notification_settings", schema = "project_management")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationSetting extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)   // one-to-one
    private User user;

    @Column(name = "on_assign", nullable = false)
    @Builder.Default
    private Boolean onAssign = true;

    @Column(name = "on_mention", nullable = false)
    @Builder.Default
    private Boolean onMention = true;

    @Column(name = "on_status_change", nullable = false)
    @Builder.Default
    private Boolean onStatusChange = true;

    @Column(name = "on_comment", nullable = false)
    @Builder.Default
    private Boolean onComment = true;
}
