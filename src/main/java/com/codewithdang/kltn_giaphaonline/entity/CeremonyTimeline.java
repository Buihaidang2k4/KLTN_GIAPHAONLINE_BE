package com.codewithdang.kltn_giaphaonline.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ceremony_timeline")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CeremonyTimeline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timeline_id")
    Long timelineId;

    @Column(name = "step_order", nullable = false)
    int stepOrder;

    @Column(name = "step_name", nullable = false)
    String stepName;

    @Column(name = "step_description")
    String stepDescription;

    @Column(name = "step_guideline")
    String stepGuideline;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    Timestamp updatedAt;

    @ManyToOne
    @JoinColumn(name = "ceremony_id", referencedColumnName = "ceremony_id")
    Ceremony ceremony;

    @Builder.Default
    @OneToMany(mappedBy = "timeline", cascade = CascadeType.ALL, orphanRemoval = true)
    List<CeremonyTimelinePreparation> preparations = new ArrayList<>();
}