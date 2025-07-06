package com.backend.server.model.repository.notification;

public interface SendNotification {
    void sendNotification(String channel, String payload);

}
