package com.codewithdang.kltn_giaphaonline.entity;


import com.codewithdang.kltn_giaphaonline.enums.RelationType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "person_relationships")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersonRelationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "relationship_id")
    Long relationshipId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id")
    Person partner;

    @Enumerated(EnumType.STRING)
    @Column(name = "relation_type", length = 20)
    RelationType relationType;

    @Column(name = "is_primary")
    Boolean isPrimary;
}