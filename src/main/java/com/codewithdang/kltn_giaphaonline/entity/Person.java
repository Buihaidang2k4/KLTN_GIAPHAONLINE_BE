package com.codewithdang.kltn_giaphaonline.entity;

import com.codewithdang.kltn_giaphaonline.enums.Gender;
import com.codewithdang.kltn_giaphaonline.enums.LifeStatus;
import jakarta.persistence.*;
import lombok.*;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "persons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    Long personId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id")
    Family family;

    @Column(name = "generation")
    Long generation;

    @Column(name = "birth_order")
    Long birthOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_person_id")
    Person rootPerson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "father_id")
    Person father;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mother_id")
    Person mother;

    @Column(name = "full_name")
    String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 20)
    Gender gender;

    @Column(name = "phone_number")
    String phoneNumber;

    @Column(name = "birth_date")
    LocalDate birthDate;

    @Column(name = "death_date")
    LocalDate deathDate;

    @Column(name = "origin_place")
    String originPlace;

    @Column(name = "place_of_residence", length = 255)
    String placeOfResidence;

    @Column(name = "grave_location", length = 255)
    String graveLocation;

    @Enumerated(EnumType.STRING)
    @Column(name = "life_status", nullable = false, length = 20)
    LifeStatus lifeStatus;

    @Column(name = "avatar_url", length = 500)
    String avatarUrl;

    @Lob
    @Column(name = "biography")
    String biography;

    @Column(name = "slug")
    String slug;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_account_id")
    Account createdByAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by_account_id")
    Account updatedByAccount;

    @Column(name = "created_at")
    Instant createdAt;

    @Column(name = "updated_at")
    Instant updatedAt;

    @Column(name = "deleted_at")
    Instant deletedAt;
}