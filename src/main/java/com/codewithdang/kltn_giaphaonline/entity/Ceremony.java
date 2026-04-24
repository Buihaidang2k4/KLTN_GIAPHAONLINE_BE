package com.codewithdang.kltn_giaphaonline.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

// phong tuc
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "ceremonies")
public class Ceremony {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ceremony_id")
    Long ceremonyId;

    @Column(name = "ceremony_type", nullable = false)
    String ceremonyType;

    @Column(name = "ceremony_name", nullable = false)
    String ceremonyName;

    @Column(name = "description")
    String description;

    @Column(name = "ceremony_path")
    String ceremonyPath;

    @Transient
    String ceremonyUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    Timestamp updatedAt;

    @ManyToOne
    @JoinColumn(name = "family_id", referencedColumnName = "family_id")
    Family family;

    @Builder.Default
    @OneToMany(mappedBy = "ceremony", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<CeremonyTimeline> ceremonyTimeline = new LinkedHashSet<>();
}