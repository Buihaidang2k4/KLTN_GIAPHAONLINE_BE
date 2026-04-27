package com.codewithdang.kltn_giaphaonline.dto.response;

import com.codewithdang.kltn_giaphaonline.enums.AchievementType;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
public class FamilyAchievementRes {
    Long achievementId;
    Long familyId;
    String personName;
    AchievementType achievementType;
    String name;
    String rank;
    String organization;
    LocalDate achievedDate;
    String description;
    String evidenceUrl;
    Instant createdAt;
    Instant updatedAt;
}
