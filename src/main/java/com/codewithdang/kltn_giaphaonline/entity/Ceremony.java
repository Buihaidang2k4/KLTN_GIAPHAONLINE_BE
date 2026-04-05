package com.codewithdang.kltn_giaphaonline.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "ceremonies") // phong tuc
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
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

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    Timestamp updatedAt;

    @ManyToOne
    @JoinColumn(name = "family_id", referencedColumnName = "family_id")
    Family family;
}