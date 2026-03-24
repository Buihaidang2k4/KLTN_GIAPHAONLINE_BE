package com.codewithdang.kltn_giaphaonline.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FamilyMemberId implements Serializable {

    @Column(name = "family_id")
    Long familyId;

    @Column(name = "account_id")
    Long accountId;
}