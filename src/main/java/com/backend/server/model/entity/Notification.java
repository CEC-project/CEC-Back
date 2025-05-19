package com.backend.server.model.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notifications")
@Getter @Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String category;      // "장비", "회원", "강의" 등의 카테고리
    private String title;         // 알림 제목
    private String message;       // 알림 상세 내용
    private String link;          // 이동할 링크
    private boolean read;
}