package com.be16_2nd.SmartFridge.notification.controller;

import com.be16_2nd.SmartFridge.common.dto.CommonDto;
import com.be16_2nd.SmartFridge.notification.dto.NotificationResDto;
import com.be16_2nd.SmartFridge.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("fridge/{fridgeId}/notification")
public class NotificationController {

    private final NotificationService notificationService;
    
    // 사용자 별 알림 목록 조회
    @GetMapping("/list")
    public ResponseEntity<?> notificationList(@PathVariable Long fridgeId,
                                              @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<NotificationResDto> notificationResDtoPage = notificationService.findNotificationList(fridgeId, pageable);
        return new ResponseEntity<>(CommonDto.builder()
                .result(notificationResDtoPage)
                .status_code(HttpStatus.OK.value())
                .status_message("알림 목록 조회 성공")
                .build()
                , HttpStatus.OK);
    }
}
