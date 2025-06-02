package com.backend.server.model.entity.enums;

public enum InquiryType {
    RENTAL,             // 대여 관련 문의
    RETURN,             // 반납 관련 문의
    SYSTEM_ERROR,       // 예약 시스템 오류
    EQUIPMENT_BROKEN,   // 장비 고장/파손 신고
    PENALTY,            // 연체/벌점 관련
    ETC                 // 기타
}