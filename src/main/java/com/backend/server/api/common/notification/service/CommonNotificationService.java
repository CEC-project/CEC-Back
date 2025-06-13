package com.backend.server.api.common.notification.service;
import java.util.List;

import com.backend.server.model.entity.User;
import com.backend.server.model.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable; 

import com.backend.server.api.common.notification.dto.CommonNotificationDto;
import com.backend.server.api.common.notification.dto.CommonNotificationResponse;
import com.backend.server.model.entity.Notification;
import com.backend.server.model.repository.notification.NotificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.backend.server.model.entity.enums.Role;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonNotificationService {
    private final NotificationRepository notificationRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

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
            String json = objectMapper.writeValueAsString(notification);
            redisTemplate.convertAndSend("notifications:" + userId, json);
        } catch (Exception e) {
            log.error("알림 전송 실패", e);
        }
    }

    public void createNotificationToAdmins(CommonNotificationDto dto) {
        // 1. 관리자 전체 조회
        List<User> allAdmins = userRepository.findAllByRole(Role.ROLE_ADMIN);
        List<User> superAdmins = userRepository.findAllByRole(Role.ROLE_SUPER_ADMIN);
        allAdmins.addAll(superAdmins);

        // 2. 각 관리자에게 알림 생성 및 전송
        for (User admin : allAdmins) {
            Notification notification = Notification.builder()
                    .userId(admin.getId())
                    .category(dto.getCategory())
                    .title(dto.getTitle())
                    .message(dto.getMessage())
                    .link(dto.getLink())
                    .read(false)
                    .build();

            notificationRepository.save(notification);

            try {
                String json = objectMapper.writeValueAsString(notification);
                redisTemplate.convertAndSend("notifications:" + admin.getId(), json);
            } catch (Exception e) {
                log.error("알림 전송 실패 (관리자 ID: " + admin.getId() + ")", e);
            }
        }
    }


    //읽음처리
    public Long changeIsReadTrue(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("그런 알림은 없어요."));

        Notification updated = notification.toBuilder()
                .read(true)
                .build();
        notificationRepository.save(updated);

        return updated.getId();
    }


    //알림조회 안읽음
    public List<CommonNotificationResponse> getAllNotReadNotification(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndReadFalse(userId);
        return notifications.stream()
            .map(CommonNotificationResponse::new)
            .toList();
    }

    public void notificationProcess(Long targetUserId, String category, String title, String message, String link) {
        // 알림 DTO 생성 및 전송
        CommonNotificationDto notification = CommonNotificationDto.builder()
                .category(category)
                .title(title)
                .message(message)
                .link(link)
                .build();
        createNotification(notification, targetUserId);
    }

    public void toAdminNotificationProcess(String category, String title, String message, String link) {
        CommonNotificationDto dto = CommonNotificationDto.builder()
                .category(category)
                .title(title)
                .message(message)
                .link(link)
                .build();
        createNotificationToAdmins(dto);
    }


}
