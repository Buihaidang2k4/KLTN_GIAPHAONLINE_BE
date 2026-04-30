package com.codewithdang.kltn_giaphaonline.dto.request;

import com.codewithdang.kltn_giaphaonline.enums.AchievementType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateFamilyAchievementReq {
    String personName;
    AchievementType achievementType;
    String name;
    String rank;
    String organization;
    LocalDate achievedDate;
    String description;
}
