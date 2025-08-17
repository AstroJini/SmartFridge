package com.be16_2nd.SmartFridge.notification.dto;

import com.be16_2nd.SmartFridge.notification.domain.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResDto {
    private List<Notification> notificationList = new ArrayList<>();
}
