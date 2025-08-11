package com.be16_2nd.SmartFridge.common.service;

import com.be16_2nd.SmartFridge.common.dto.SseMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(String senderEmail, String receiverEmail, String contents, String type) {
        SseMessageDTO sseMessageDTO = SseMessageDTO.builder()
                .type(type)
                .senderEmail(senderEmail)
                .receiverEmail(receiverEmail)
                .contents(contents)
                .build();

        log.info("Redis 채널로 알림 발행. 수신자: {}", receiverEmail);
        redisTemplate.convertAndSend("notification-channel", sseMessageDTO);
    }
}
