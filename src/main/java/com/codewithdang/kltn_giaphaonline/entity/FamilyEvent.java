package com.codewithdang.kltn_giaphaonline.entity;

import com.codewithdang.kltn_giaphaonline.enums.CalendarType;
import com.codewithdang.kltn_giaphaonline.enums.FamilyEventStatus;
import com.codewithdang.kltn_giaphaonline.enums.ReminderEventType;
import com.codewithdang.kltn_giaphaonline.enums.RepeatType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

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
    @JoinColumn(name = "family_id", nullable = false)
    Family family;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_account_id", nullable = false)
    Account createdByAccount;

    @Column(name = "event_name")
    String eventName;

    @Column(name = "event_time")
    LocalTime eventTime;

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
    @Column(name = "reminder_type", length = 20)
    ReminderEventType reminderType;

    // Lưu địa danh hành chính hoặc tên gợi nhớ.
    @Column(name = "location", length = 500)
    String location;

    // Lưu dữ liệu để hiển thị bản đồ.
    @Column(name = "location_map_url", columnDefinition = "TEXT")
    String LocationMapUrl;

    @Column(name = "note", length = 500)
    String note;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    FamilyEventStatus status;

    @CreationTimestamp
    @Column(name = "created_at")
    Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    Instant updatedAt;
}