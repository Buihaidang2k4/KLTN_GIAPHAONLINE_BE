package com.codewithdang.kltn_giaphaonline.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DashboardRes {
    Long totalMembersInFamilyTree;
    Long totalEvents;
    Long totalAlbumMedias;
    Long totalAchievement;
    Long totalCustoms;
    Long totalAdmin;
    Long totalStorages;
    Long totalUsedStorages;
    String currentFamilyName;
    String currentSubscriptionPlanName;
    String currentSubscriptionPlanPrice;
    LocalDate currentSubscriptionStartDate;
    LocalDate currentSubscriptionEndDate;
}