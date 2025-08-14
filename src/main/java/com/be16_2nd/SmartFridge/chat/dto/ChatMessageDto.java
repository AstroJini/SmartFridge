package com.be16_2nd.SmartFridge.chat.dto;

import com.be16_2nd.SmartFridge.chat.domain.ChatMessage;
import com.be16_2nd.SmartFridge.chat.domain.ChatRoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChatMessageDto {
    private Long roomId;
    private String chatRoomType;
    private String message;
    private String senderEmail;

    private String timestamp;

    public static ChatMessageDto fromEntityChatManager(ChatMessage chatMessage){
        return ChatMessageDto.builder()
                .roomId(chatMessage.getManagerChatRoom().getId())
                .chatRoomType(chatMessage.getChatRoomType().toString())
                .timestamp(chatMessage.getCreatedTime().toString())
                .message(chatMessage.getContents())
                .senderEmail(chatMessage.getSender().getEmail())
                .build();
    }
}
