package com.backend.server.model.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    // 설명 수정. 스웨거 상에서 Enum 값이 한글로 뜸.
    AVAILABLE("AVAILABLE(대여 가능)"),
    IN_USE("IN_USE(대여중)"),
    BROKEN("BROKEN(파손)"),
    RENTAL_PENDING("RENTAL_PENDING(대여 신청 중)"),
    RETURN_PENDING("RETURN_PENDING(반납 처리중)");
    private final String description;

    @Override
    public String toString() {
        return description;
    }
} 