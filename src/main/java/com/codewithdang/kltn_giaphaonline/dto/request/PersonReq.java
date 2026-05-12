package com.codewithdang.kltn_giaphaonline.dto.request;

import com.codewithdang.kltn_giaphaonline.enums.Gender;
import com.codewithdang.kltn_giaphaonline.enums.LifeStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersonReq {
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
}