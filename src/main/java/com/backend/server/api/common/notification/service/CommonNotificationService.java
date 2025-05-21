package com.backend.server.api.common.notification.service;
import java.util.List;

import com.backend.server.api.common.notification.dto.NotificationIdResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable; 

import com.backend.server.api.common.notification.dto.CommonNotificationDto;
import com.backend.server.api.common.notification.dto.CommonNotificationResponse;
import com.backend.server.model.entity.Notification;
import com.backend.server.model.repository.notification.NotificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonNotificationService {
    private final NotificationRepository notificationRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    
    public void createNotification(CommonNotificationDto dto, Long userId){
        Notification notification = Notification.builder()
            .userId(userId)
            .category(dto.getCategory())
            .title(dto.getTitle())
            .message(dto.getMessage())
            .link(dto.getLink())
            .read(false)
            .build();
        
        notificationRepository.save(notification);
        
        // Redis로 실시간 알림 발송
        try {
            redisTemplate.convertAndSend(
                "notifications:" + userId, 
                notification
            );
        } catch (Exception e) {
            log.error("실시간 알림 발송 실패", e);
        }
    }

    //읽음처리
    public NotificationIdResponse changeIsReadTrue(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("그런 알림은 없어요."));

        Notification updated = notification.toBuilder()
                .read(true)
                .build();

        notificationRepository.save(updated);

        return new NotificationIdResponse(updated);
    }


    //알림조회 안읽음
    public List<CommonNotificationResponse> getAllNotReadNotification(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndReadFalse(userId);
        return notifications.stream()
            .map(CommonNotificationResponse::new)
            .toList();
    }

    //알림조회 전체
    public List<CommonNotificationResponse> getAllNotification(Long userId, Pageable pagealbe) {
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pagealbe);
        return notifications.stream()
            .map(CommonNotificationResponse::new)
            .toList();
    }

}
