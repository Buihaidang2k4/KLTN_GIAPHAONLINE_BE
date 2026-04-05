package com.codewithdang.kltn_giaphaonline.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "ceremony_timeline_preparations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CeremonyTimelinePreparation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "preparation_id")
    Long preparationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeline_id", nullable = false)
    CeremonyTimeline timeline;

    @Column(name = "item_name", nullable = false)
    String itemName;

    @Column(name = "item_type", length = 50)
    String itemType;

    @Column(name = "quantity")
    Integer quantity = 1;

    @Column(name = "unit", length = 50)
    String unit;

    @Column(name = "note", columnDefinition = "text")
    String note;

    @Column(name = "required", nullable = false)
    boolean required = true;
}