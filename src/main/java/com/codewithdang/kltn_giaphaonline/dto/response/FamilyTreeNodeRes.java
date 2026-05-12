package com.codewithdang.kltn_giaphaonline.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FamilyTreeNodeRes {
    String id;
    String fid;
    String mid;
    List<String> pids;
    String generation;
    String personName;
    String phoneNumber;
    String gender;
    String avatarPath;
    String avatarUrl;
    String birthDate;
    String deathDate;
    String biography;
    String lifeStatus;
    String originPlace;
    String placeOfResidence;
    Boolean isInFamily; // true = thuộc dòng họ, false = partner ngoài
}
