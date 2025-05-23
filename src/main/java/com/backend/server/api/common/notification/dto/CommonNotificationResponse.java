package com.backend.server.api.common.notification.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.backend.server.model.entity.Notification;

import lombok.Getter;

@Getter
public class CommonNotificationResponse {
    private Long id;
    private String totalMessage;
    private String link;
    private LocalDateTime createdAt;
    private boolean read;

    public CommonNotificationResponse(Notification notification){
        this.id = notification.getId();
        this.totalMessage = "[" + notification.getCategory() + "] " + notification.getMessage();
        this.link = notification.getLink();
        this.createdAt = notification.getCreatedAt();
        this.read = notification.isRead();
    }
}
