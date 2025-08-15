package com.be16_2nd.SmartFridge.chat.service;

import com.be16_2nd.SmartFridge.chat.domain.ManagerChatRoom;
import com.be16_2nd.SmartFridge.chat.repository.ManagerChatRoomRepository;
import com.be16_2nd.SmartFridge.fridge.domain.Fridge;
import com.be16_2nd.SmartFridge.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class ChatRoomLifecycle {

    private final ManagerChatRoomRepository managerChatRoomRepository;

    //        채팅방 생성
    public void generateManagerChatRoom(Member member, Fridge fridge){
        ManagerChatRoom newChatRoom = ManagerChatRoom.builder()
                .fridge(fridge)
                .member(member)
                .build();
        managerChatRoomRepository.save(newChatRoom);
    }

    // 냉장고 나갈 시 채팅방 삭제
    public void deleteManagerChatRoom(Member member, Fridge fridge){
        // 1대1채팅
        managerChatRoomRepository.deleteByFridgeAndMember(fridge, member);
    }
    
    // 회원 탈퇴 시 모든 채팅방 삭제
}
