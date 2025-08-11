package com.be16_2nd.SmartFridge.notification.repository;

import com.be16_2nd.SmartFridge.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
