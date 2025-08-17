package com.be16_2nd.SmartFridge.chat.domain;

import com.be16_2nd.SmartFridge.fridge.domain.Fridge;
import com.be16_2nd.SmartFridge.member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class PurchaseChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fridge_id", nullable = false)
    private Fridge fridge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private Member creator;

    @Column(nullable = false)
    private String title;

    private String contents;

    @Column(nullable = false)
    private Integer limitedNum;

    @Column(nullable = false)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private JoinStatus joinStatus = JoinStatus.OPEN;

    private String productUrl;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActived = true;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdTime;

    private LocalDateTime lastMessageAt;
}
