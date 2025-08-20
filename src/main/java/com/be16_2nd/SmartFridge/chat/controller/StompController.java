package com.be16_2nd.SmartFridge.chat.controller;

import com.be16_2nd.SmartFridge.chat.domain.ChatMessage;
import com.be16_2nd.SmartFridge.chat.domain.ChatParticipant;
import com.be16_2nd.SmartFridge.chat.dto.ChatMessageDto;
import com.be16_2nd.SmartFridge.chat.service.ChatRedisPubSubService;
import com.be16_2nd.SmartFridge.chat.service.ChatService;
import com.be16_2nd.SmartFridge.fridge.domain.Fridge;
import com.be16_2nd.SmartFridge.fridge.domain.FridgeMember;
import com.be16_2nd.SmartFridge.fridge.domain.Type;
import com.be16_2nd.SmartFridge.fridge.repository.FridgeMemberRepository;
import com.be16_2nd.SmartFridge.member.domain.Member;
import com.be16_2nd.SmartFridge.notification.domain.NotificationType;
import com.be16_2nd.SmartFridge.notification.service.NotificationPublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@Slf4j
public class StompController {
    private final ChatService chatService;
    private final ChatRedisPubSubService chatRedisPubSubService;
    private final NotificationPublisher notificationPublisher;
    private final FridgeMemberRepository fridgeMemberRepository;

    @MessageMapping("MANAGER/{roomId}")
    public void sendMessageToManager(@DestinationVariable Long roomId, ChatMessageDto chatMessageReqDto) throws JsonProcessingException {
        log.error("@@@@@@@@@@@@@ 메세지 : {}", chatMessageReqDto);
        chatMessageReqDto.setChatRoomType("MANAGER");
        chatMessageReqDto.setRoomId(roomId);
        ChatMessage chatMessage = chatService.saveMessage(roomId, chatMessageReqDto);
        ChatMessageDto chatMessageDto = ChatMessageDto.fromEntityManagerChat(chatMessage);
        ObjectMapper objectMapper = new ObjectMapper();
        String message = objectMapper.writeValueAsString(chatMessageDto);
        chatRedisPubSubService.publish("chat-channel", message);

        Fridge fridge = chatMessage.getManagerChatRoom().getFridge();

        FridgeMember managerFridgeMember = fridgeMemberRepository
                .findByFridgeAndType(fridge, Type.MANAGER)
                .orElseThrow(() -> new RuntimeException("관리자가 존재하지 않습니다."));

        Member receiver = managerFridgeMember.getMember();
        Member sender = chatMessage.getSender();

            // 발신자가 냉장고 관리자면, 수신자는 일반 사용자
            // 수신자가 일반 사용자면, 발신자가 냉장고 관리자
        if (sender.getId().equals(receiver.getId())) {
            receiver = chatMessage.getManagerChatRoom().getMember();
        }
        notificationPublisher.publish(
                chatMessage.getSender().getEmail(),
                receiver.getEmail(),
                "새 메시지가 도착했습니다",
                NotificationType.ADMIN_CHAT.name()
        );
    }
    @MessageMapping("purchase/{roomId}")
    public void sendMessageToGroup(@DestinationVariable Long roomId, ChatMessageDto chatMessageReqDto) throws JsonProcessingException {
        chatMessageReqDto.setChatRoomType("PURCHASE");
        chatMessageReqDto.setRoomId(roomId);
        ChatMessage chatMessage = chatService.saveMessage(roomId, chatMessageReqDto);
        ChatMessageDto chatMessageDto = ChatMessageDto.fromEntityPurchaseChat(chatMessage);
        ObjectMapper objectMapper = new ObjectMapper();
        String message = objectMapper.writeValueAsString(chatMessageDto);
        chatRedisPubSubService.publish("chat-channel", message);
        
        // 채팅방 참여자 모두에게 발송
        List<Member> receivers = chatMessage.getPurchaseChatRoom().getParticipants().stream()
                .map(ChatParticipant::getMember)
                .filter(member -> !member.getId().equals(chatMessage.getSender().getId()))
                .toList();

        for (Member receiver : receivers) {
            notificationPublisher.publish(
                    chatMessage.getSender().getEmail(),
                    receiver.getEmail(),
                    "공동 구매 채팅에 새 메시지가 도착했습니다",
                    NotificationType.GROUP_CHAT.name()

            );
        }
    }
}
