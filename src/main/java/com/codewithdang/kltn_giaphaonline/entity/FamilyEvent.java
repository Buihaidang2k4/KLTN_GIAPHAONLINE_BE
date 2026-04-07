package com.codewithdang.kltn_giaphaonline.entity;

import com.codewithdang.kltn_giaphaonline.enums.CalendarType;
import com.codewithdang.kltn_giaphaonline.enums.FamilyEventStatus;
import com.codewithdang.kltn_giaphaonline.enums.NotificationEventType;
import com.codewithdang.kltn_giaphaonline.enums.RepeatType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "family_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FamilyEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "family_event_id")
    Long familyEventId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id")
    Family family;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_account_id")
    Account createdByAccount;

    @Column(name = "event_name")
    String eventName;

    @Column(name = "solar_date")
    LocalDate solarDate;

    @Column(name = "lunar_date")
    LocalDate lunarDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "calendar_type", length = 20)
    CalendarType calendarType;

    @Enumerated(EnumType.STRING)
    @Column(name = "repeat_type", length = 20)
    RepeatType repeatType;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", length = 20)
    NotificationEventType notificationEventType;

    @Column(name = "location", length = 255)
    String location;

    @Column(name = "description", length = 500)
    String description;

    @Column(name = "note", length = 500)
    String note;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    FamilyEventStatus status;

    @Column(name = "remind_before_days")
    Integer remindBeforeDays;

    @Column(name = "created_at")
    Instant createdAt;

    @Column(name = "updated_at")
    Instant updatedAt;
}