package com.backend.server.model.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    AVAILABLE("대여 가능"),
    IN_USE("대여중"),
    BROKEN("파손"),
    RENTAL_PENDING("대여 신청 중"),
    RETURN_PENDING("반납 처리중");
    private final String description;

    @Override
    public String toString() {
        return description;
    }
} 