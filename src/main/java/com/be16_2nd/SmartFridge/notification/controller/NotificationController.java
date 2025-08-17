package com.be16_2nd.SmartFridge.notification.controller;

import com.be16_2nd.SmartFridge.common.dto.CommonDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {
    
    // 사용자 별 알림 목록 조회
    @GetMapping("/list")
    public ResponseEntity<?> notificationList() {
        return new ResponseEntity<>(CommonDto.builder().build(), HttpStatus.OK);
    }
}
