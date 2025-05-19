package com.backend.server.api.common.notification.dto;

import com.backend.server.model.entity.Notification;
import lombok.Getter;

@Getter
public class NotificationIdResponse {
    private Long id;

    public NotificationIdResponse(Notification notification)
    {
        this.id = id;
    }
}
