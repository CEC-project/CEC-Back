package com.backend.server.api.common.notification.service;

public interface SendNotification {
    void sendNotification(String channel, String payload);

}
