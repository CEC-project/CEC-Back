package com.backend.server.model.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RentalStatus {
    AVAILABLE("대여 가능"),
    IN_USE("대여중"),
    REPAIR("수리중");

    private final String description;

    @Override
    public String toString() {
        return description;
    }
} 