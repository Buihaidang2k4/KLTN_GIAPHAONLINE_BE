package com.codewithdang.kltn_giaphaonline.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FamilyTreeNodeRes {
    Long id;
    Long fid;
    Long mid;
    List<Long> pids;
    List<Long> childs;
    String fidName;
    String midName;
    Long generation;
    String personName;
    String phoneNumber;
    String gender;
    String avatarPath;
    String avatarUrl;
    LocalDate birthDate;
    LocalDate deathDate;
    String biography;
    String lifeStatus;
    String originPlace;
    String placeOfResidence;
    Boolean isInFamily;
}
