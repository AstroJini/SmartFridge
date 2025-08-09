package com.be16_2nd.SmartFridge.notification.service;

import com.be16_2nd.SmartFridge.fridge.domain.Fridge;
import com.be16_2nd.SmartFridge.member.domain.Member;
import com.be16_2nd.SmartFridge.notification.domain.Notification;
import com.be16_2nd.SmartFridge.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void create(Member sender, Member receiver, Fridge fridge,  String content) {
        Notification notification = Notification.builder()
                .sender(sender)
                .receiver(receiver)
                .fridge(fridge)
                .content(content)
                .isRead(false)
                .build();

        notificationRepository.save(notification);
    }

}
