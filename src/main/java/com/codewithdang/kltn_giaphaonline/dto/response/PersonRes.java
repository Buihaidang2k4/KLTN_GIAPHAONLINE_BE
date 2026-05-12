
package com.codewithdang.kltn_giaphaonline.dto.response;

import com.codewithdang.kltn_giaphaonline.enums.Gender;
import com.codewithdang.kltn_giaphaonline.enums.LifeStatus;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersonRes {
    Long personId;
    Long familyCategoryId;
    Long rootPersonId;
    Long fatherId;
    Long motherId;
    Long createdByAccountId;
    Long generation;
    Long birthOrder;
    String fullName;
    Gender gender;
    String phoneNumber;
    LocalDate birthDate;
    LocalDate deathDate;
    String originPlace;
    String placeOfResidence;
    String graveLocation;
    LifeStatus lifeStatus;
    String avatarUrl;
    String biography;
    String slug;
    Instant createdAt;
    Instant updatedAt;
    Instant deletedAt;
}