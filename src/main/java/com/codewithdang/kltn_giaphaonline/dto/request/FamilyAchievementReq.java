package com.codewithdang.kltn_giaphaonline.dto.request;

import com.codewithdang.kltn_giaphaonline.enums.AchievementType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FamilyAchievementReq {

    @NotBlank(message = "Tên người không được để trống")
    String personName;

    @NotNull(message = "Loại thành tích không được để trống")
    AchievementType achievementType;

    @NotBlank(message = "Tên thành tích không được để trống")
    String name;

    String rank;
    String organization;
    LocalDate achievedDate;
    String description;
    String evidenceUrl;
}
