package com.codewithdang.kltn_giaphaonline.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "family_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FamilyCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "family_category_id")
    Long familyCategoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    Account owner;

    @Column(name = "family_name")
    String familyName;

    @Column(name = "origin")
    String origin;

    @Column(name = "description", columnDefinition = "text")
    String description;

    @Column(name = "is_public", nullable = false)
    Boolean isPublic;

    @Builder.Default
    @Column(name = "total_person", nullable = false, columnDefinition = "bigint default 0")
    Long totalPerson = 0L;

    @Builder.Default
    @Column(name = "generation_offset", nullable = false, columnDefinition = "bigint default 1")
    Long generationOffset = 1L;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "family_id", referencedColumnName = "family_id")
    Family family;
}