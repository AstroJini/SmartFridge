package com.be16_2nd.SmartFridge.notification.service;

import com.be16_2nd.SmartFridge.common.service.NotificationPublisher;
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
    private final NotificationPublisher notificationPublisher;

    public void create(Notification notification) {
        notificationPublisher.publish(
                notification.getSender().getEmail()
                , notification.getReceiver().getEmail()
                , notification.getContent()
                ,notification.getNotificationType().name()
        );

        notificationRepository.save(notification);
    }

}
