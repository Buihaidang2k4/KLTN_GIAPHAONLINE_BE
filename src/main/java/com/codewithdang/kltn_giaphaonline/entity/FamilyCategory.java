package com.codewithdang.kltn_giaphaonline.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "family_categories")  // Thay đổi tên bảng thành "family_categories"
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FamilyCategory {  // Đổi tên class từ Family thành FamilyCategory
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "family_category_id")  // Đổi tên trường thành "family_category_id"
    Long familyCategoryId;  // Đổi tên ID thành "familyCategoryId"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    Account owner;

    @Column(name = "family_name")
    String familyName;

    @Column(name = "origin")
    String origin;

    @Column(name = "description", columnDefinition = "text")
    String description;

    @Column(name = "left_couplet", length = 255)
    String leftCouplet;

    @Column(name = "right_couplet", length = 255)
    String rightCouplet;

    @Column(name = "background_template_url", length = 500)
    String backgroundTemplateUrl;

    @Column(name = "is_public", nullable = false)
    Boolean isPublic;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    protected Instant updatedAt;

    @Builder.Default
    @OneToMany(mappedBy = "familyCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<FamilyMember> members = new LinkedHashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "familyCategory")
    Set<FamilyInvitation> invitations = new LinkedHashSet<>();
}