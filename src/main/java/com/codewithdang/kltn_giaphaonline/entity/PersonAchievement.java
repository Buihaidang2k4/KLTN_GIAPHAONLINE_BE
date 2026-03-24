package com.codewithdang.kltn_giaphaonline.entity;


import com.codewithdang.kltn_giaphaonline.enums.AchievementType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "person_achievements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersonAchievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "achievement_id")
    Long achievementId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_id", nullable = false)
    Person person;

    @Enumerated(EnumType.STRING)
    @Column(name = "achievement_type", length = 20)
    AchievementType achievementType;

    @Column(name = "name", nullable = false, length = 255)
    String name;

    @Column(name = "organization", length = 255)
    String organization;

    @Column(name = "achieved_date")
    LocalDate achievedDate;

    @Column(name = "description", length = 500)
    String description;

    @Column(name = "evidence_url", length = 500)
    String evidenceUrl;

    @Column(name = "created_at")
    Instant createdAt;

    @Column(name = "updated_at")
    Instant updatedAt;
}