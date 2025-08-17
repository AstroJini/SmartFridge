package com.be16_2nd.SmartFridge.chat.controller;

import com.be16_2nd.SmartFridge.chat.domain.ChatRoomType;
import com.be16_2nd.SmartFridge.chat.dto.ChatMessageDto;
import com.be16_2nd.SmartFridge.chat.dto.ChatRoomCreateDto;
import com.be16_2nd.SmartFridge.chat.dto.PurchaseChatRoomListResDto;
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

    //    관리자 채팅방 이전 메시지 조회
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
    // 공동 구매 채팅방 이전 메시지 조회
    @GetMapping("/history/purchase/{roomId}")
    public ResponseEntity<?> getPurchaseChatHistory(@PathVariable Long roomId) {
        List<ChatMessageDto> chatMessageDtos = chatService.getPurchaseChatHistory(roomId);
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
                .result("ok")
                .status_code(HttpStatus.OK.value())
                .status_message("채팅내역 읽음 처리 성공")
                .build(), HttpStatus.OK);
    }

    // 공동구매 채팅방 개설
    @PostMapping("/purchase/room/create")
    public ResponseEntity<?> createPurchaseRoom(@RequestBody ChatRoomCreateDto chatRoomCreateDto){
        return new ResponseEntity<>(CommonDto.builder()
                .result(chatService.createPurchaseChatRoom(chatRoomCreateDto))
                .status_code(HttpStatus.CREATED.value())
                .status_message("공동구매 채팅방 개설 성공")
                .build(), HttpStatus.CREATED);
    }

    //    그룹채팅목록조회
    @GetMapping("/purchase/room/list/{fridgeId}")
    public ResponseEntity<?> getPurchaseChatRooms(@PathVariable Long fridgeId){
        return new ResponseEntity<>(CommonDto.builder()
                .result(chatService.getPurchaseChatRooms(fridgeId))
                .status_code(HttpStatus.OK.value())
                .status_message("공동구매 채팅방 목록 조회 성공")
                .build(), HttpStatus.OK);
    }

    //    공동구매채팅방참여
    @PostMapping("/purchase/room/{roomId}/join")
    public ResponseEntity<?> joinPurchaseChatRoom(@PathVariable Long roomId){
        chatService.addParticipantToPurchaseChat(roomId);
        return new ResponseEntity<>(CommonDto.builder()
                .result("ok")
                .status_code(HttpStatus.OK.value())
                .status_message("공동구매 채팅방 참여 성공")
                .build(), HttpStatus.OK);
    }
}
