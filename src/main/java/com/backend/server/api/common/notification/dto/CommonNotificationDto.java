package com.backend.server.api.common.notification.dto;

import com.backend.server.model.entity.Notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonNotificationDto {
    private Long id;
    private String category;
    private String title;
    private String message;
    private String link;
    private boolean read;    
    
    public String getFormattedMessage() {
        return "[" + category + "] " + title;
    }
    
    // Entity <-> DTO 변환 메소드
    public CommonNotificationDto(Notification notification){
        this.id = notification.getId();
        this.category = notification.getCategory();
        this.title = notification.getTitle();
        this.message = notification.getMessage();
        this.link = notification.getLink();
        this.read = notification.isRead();
    }
}