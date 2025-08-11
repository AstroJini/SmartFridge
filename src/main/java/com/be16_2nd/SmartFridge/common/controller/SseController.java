package com.be16_2nd.SmartFridge.common.controller;

import com.be16_2nd.SmartFridge.common.service.SseEmitterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/sse")
public class SseController {

    private final SseEmitterRegistry sseEmitterRegistry;

    @GetMapping("/connect")
    public SseEmitter subscribe() {
        SseEmitter sseEmitter = new SseEmitter(14400 * 60 * 1000L);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("SSE 연결 시도됨. 사용자 email: {}", email);
        sseEmitterRegistry.addSseEmitter(email, sseEmitter);
        try {
            sseEmitter.send(SseEmitter.event().name("connect").data("연결 완료"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sseEmitter;
    }


    @GetMapping("/disconnect")
    public void unSubscribe() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        sseEmitterRegistry.removeEmitter(email);
    }
}
