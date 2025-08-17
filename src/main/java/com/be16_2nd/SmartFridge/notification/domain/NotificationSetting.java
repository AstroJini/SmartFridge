package com.be16_2nd.SmartFridge.notification.domain;

import com.be16_2nd.SmartFridge.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uk_notification_setting_user_type", columnNames = {"user_id", "type"})
})
@Builder
public class NotificationSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationSettingType notificationSettingType;

    @Column(nullable = false)
    @Builder.Default
    private boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
}

