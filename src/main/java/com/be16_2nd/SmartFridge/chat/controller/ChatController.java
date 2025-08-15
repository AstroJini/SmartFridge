package com.be16_2nd.SmartFridge.chat.controller;

import com.be16_2nd.SmartFridge.chat.domain.ChatRoomType;
import com.be16_2nd.SmartFridge.chat.dto.ChatMessageDto;
import com.be16_2nd.SmartFridge.chat.dto.MyChatListResDto;
import com.be16_2nd.SmartFridge.chat.service.ChatService;
import com.be16_2nd.SmartFridge.common.dto.CommonDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    // 냉장고 관리자 채팅은 fridge 서비스에서 구현

    // 내 채팅 목록
    @GetMapping("/my/rooms")
    public ResponseEntity<?> getMyChatRooms() {
        // 후에 냉장고 ID 받아와야 함
        List<MyChatListResDto> myChatListResDtos = chatService.getMyChatRooms(1L);
        return new ResponseEntity<>(CommonDto.builder()
                .result(myChatListResDtos)
                .status_code(HttpStatus.OK.value())
                .status_message("내 채팅 목록 조회 성공")
                .build(), HttpStatus.OK);
    }

    //    관리자 이전 메시지 조회
    // 냉장고 id param으로 받음
    @GetMapping("/history/manager/{roomId}")
    public ResponseEntity<?> getManagerChatHistory(@PathVariable Long roomId) {
        List<ChatMessageDto> chatMessageDtos = chatService.getManagerChatHistory(roomId);
        return new ResponseEntity<>(CommonDto.builder()
                .result(chatMessageDtos)
                .status_code(HttpStatus.OK.value())
                .status_message("채팅내역 조회 성공")
                .build(), HttpStatus.OK);
    }

    //    관리자 채팅메시지 읽음처리
    @PostMapping("/manager/room/{roomId}/read")
    public ResponseEntity<?> readChatRoom(@PathVariable Long roomId) {
        chatService.messageRead(roomId, ChatRoomType.MANAGER);
        return new ResponseEntity<>(CommonDto.builder()
                .result("채팅내역 조회 성공")
                .status_code(HttpStatus.OK.value())
                .status_message("ok")
                .build(), HttpStatus.OK);
    }
}
