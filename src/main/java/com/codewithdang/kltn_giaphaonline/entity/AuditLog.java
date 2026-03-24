package com.codewithdang.kltn_giaphaonline.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "audit_logs",
        indexes = {
                @Index(name = "idx_audit_actor", columnList = "actor_account_id"),
                @Index(name = "idx_audit_action", columnList = "action"),
                @Index(name = "idx_audit_entity_type", columnList = "entity_type"),
                @Index(name = "idx_audit_created_at", columnList = "created_at")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id")
    Long auditId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_account_id")
    Account actor;

    @Column(name = "family_id")
    Long familyId;

    @Column(name = "action", nullable = false, length = 100)
    String action;

    @Column(name = "entity_type", nullable = false, length = 100)
    String entityType;

    @Column(name = "entity_id", length = 100)
    String entityId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    Map<String, Object> oldData;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    Map<String, Object> newData;

    @Column(name = "ip_address", length = 45)
    String ipAddress;

    @Column(name = "user_agent", length = 255)
    String userAgent;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    Instant createdAt;
}