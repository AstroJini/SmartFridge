package com.be16_2nd.SmartFridge.chat.service;

import com.be16_2nd.SmartFridge.chat.domain.ChatMessage;
import com.be16_2nd.SmartFridge.chat.domain.ChatRoomType;
import com.be16_2nd.SmartFridge.chat.domain.ManagerChatRoom;
import com.be16_2nd.SmartFridge.chat.dto.ChatMessageDto;
import com.be16_2nd.SmartFridge.chat.dto.MyChatListResDto;
import com.be16_2nd.SmartFridge.chat.repository.ChatMessageRepository;
import com.be16_2nd.SmartFridge.chat.repository.ManagerChatRoomRepository;
import com.be16_2nd.SmartFridge.fridge.domain.Fridge;
import com.be16_2nd.SmartFridge.fridge.repository.FridgeRepository;
import com.be16_2nd.SmartFridge.member.domain.Member;
import com.be16_2nd.SmartFridge.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/*
* 채팅 서비스
*/

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ChatService {

    private final MemberRepository memberRepository;
    private final ManagerChatRoomRepository managerChatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final FridgeRepository fridgeRepository;

    public ChatMessage saveMessage(Long roomId, ChatMessageDto chatMessageDto) {

        Member sender = memberRepository.findByEmail(chatMessageDto.getSenderEmail()).orElseThrow(()->new EntityNotFoundException("room cannot find"));

        ChatMessage chatMessage = new ChatMessage();
        
        // 냉장고 관리자와의 1대1 채팅일 경우
        if(chatMessageDto.getChatRoomType().equals("MANAGER")){
            ManagerChatRoom chatRoom = managerChatRoomRepository.findById(roomId).orElseThrow(()->new EntityNotFoundException("room cannot find"));
            chatMessage = ChatMessage.builder()
                    .chatRoomType(ChatRoomType.MANAGER)
                    .managerChatRoom(chatRoom)
                    .sender(sender)
                    .contents(chatMessageDto.getMessage())
                    .isDeleted(false)
                    .build();
        // 그룹 채팅일 경우
        }else{
//            ManagerChatRoom chatRoom = managerChatRoomRepository.findById(roomId).orElseThrow(()->new EntityNotFoundException("room cannot find"));
//            ChatMessage chatMessage = ChatMessage.builder()
//                    .chatRoomType(chatMessageDto.getChatRoomType())
//                    .managerChatRoom(chatRoom)
//                    .sender(sender)
//                    .contents(chatMessageDto.getMessage())
//                    .isDeleted(false)
//                    .build();
        }
        return chatMessageRepository.save(chatMessage);

//        사용자별로 읽음여부 저장
//        List<ChatRoomMember> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);
//        for(ChatParticipant chatParticipant : chatParticipants) {
//            ReadStatus readStatus = ReadStatus.builder()
//                    .chatRoom(chatRoom)
//                    .member(chatParticipant.getMember())
//                    .chatMessage(chatMessage)
//                    .isRead(chatParticipant.getMember().equals(sender))
//                    .build();
//            readStatusRepository.save(readStatus);
//        }
    }

    public List<MyChatListResDto> getMyChatRooms(Long fridgeId){
        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new EntityNotFoundException("member not found"));
        Fridge fridge = fridgeRepository.findById(fridgeId).orElseThrow(()->new EntityNotFoundException("fridge not found"));

        List<MyChatListResDto> chatListResDtos = new ArrayList<>();

        // 관리자 채팅 방
        ManagerChatRoom managerChatRoom = managerChatRoomRepository.findByFridgeAndMember(fridge, member);
        if(managerChatRoom != null){
            MyChatListResDto myChatListResDto = MyChatListResDto.builder()
                    .roomId(managerChatRoom.getId())
                    .roomName("관리자와의 채팅")
                    .build();
            chatListResDtos.add(myChatListResDto);
        }

        // 공유 채팅방
        return chatListResDtos;
    }
    
    // 채팅 내역 조회
    public List<ChatMessageDto> getManagerChatHistory(Long roomId) {
        // 채팅방 찾기
        ManagerChatRoom managerChatRoom = managerChatRoomRepository.findById(roomId).orElseThrow(()->new EntityNotFoundException("room cannot find"));
        // 메시지 조회
        List<ChatMessage>chatMessages = chatMessageRepository.findByManagerChatRoomOrderByCreatedTimeAsc(managerChatRoom);
        List<ChatMessageDto> chatMessageDtos = new ArrayList<>();
        for(ChatMessage chatMessage : chatMessages){
            ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                    .chatRoomType(chatMessage.getChatRoomType().toString())
                    .message(chatMessage.getContents())
                    .senderEmail(chatMessage.getSender().getEmail())
                    .timestamp(chatMessage.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH시 mm분")))
                    .build();
            chatMessageDtos.add(chatMessageDto);
        }
        return chatMessageDtos;
    }
}
