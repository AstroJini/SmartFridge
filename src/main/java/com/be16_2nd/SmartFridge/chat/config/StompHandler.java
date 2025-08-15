package com.be16_2nd.SmartFridge.chat.config;

import com.be16_2nd.SmartFridge.chat.service.ChatService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

/*
* Stomp 인터셉터
*/

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    @Value("${jwt.secretKeyAt}")
    private String secretKey;

    private final ChatService chatService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        
        final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // CONNECT 요청 시
        if(StompCommand.CONNECT == accessor.getCommand()) {
            log.info("stomp connect 요청");
            String bearerToken = accessor.getFirstNativeHeader("Authorization");
            String token = bearerToken.substring(7);
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            log.info("토큰 검증 완료");
        }
        // SUBSCRIBE 요청 시
        if(StompCommand.SUBSCRIBE == accessor.getCommand()){
            log.info("stomp subscribe 요청");
            String bearerToken = accessor.getFirstNativeHeader("Authorization");
            String token = bearerToken.substring(7);
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            String email = claims.getSubject();
            String roomType = accessor.getDestination().split("/")[3];
            String roomId = accessor.getDestination().split("/")[4];

            // 1대1 채팅방
            if(roomType.equals("MANAGER")){
                if(!chatService.isManagerRoomParticipant(email, Long.parseLong(roomId))){
                    throw new AccessDeniedException("UNAUTHORIZED");
                }else{
                }

            // 공동 구매 채팅방
            }else{

            }

        }

        // 구독 끊을 경우 현재 접속 중인 사용자 제거
        if(StompCommand.UNSUBSCRIBE == accessor.getCommand()){
            System.out.println(accessor.getNativeHeader("id"));
        }
        return message;
    }
}

