package com.be16_2nd.SmartFridge.chat.domain;

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
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_chat_id")
    private ManagerChatRoom managerChatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private Member sender;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdTime;

    private String contents;

    private boolean isDeleted;

    private ChatRoomType chatRoomType;
}
