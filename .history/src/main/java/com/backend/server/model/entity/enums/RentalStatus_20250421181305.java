package com.backend.server.model.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RentalStatus {
    AVAILABLE("대여 가능"),
    IN_USE("대여중"),
    REPAIR("수리중"),
    PENDING("대여 신청 중"),
    APPROVED("대여 승인됨"),
    REJECTED("대여 거절됨"),
    RETURNED("반납 완료");
    private final String description;

    @Override
    public String toString() {
        return description;
    }
} 