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

    public void publish(String sender, String receiver, String contents, String type) {
        SseMessageDTO sseMessageDTO = SseMessageDTO.builder()
                .type(type)
                .sender(sender)
                .receiver(receiver)
                .contents(contents)
                .build();

        log.info("Redis 채널로 알림 발행. 수신자: {}", receiver);
        redisTemplate.convertAndSend("notification-channel", sseMessageDTO);
    }
}
