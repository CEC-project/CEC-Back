package com.backend.server.model.entity.enums;

public enum Gender {
    M, F;
    public static Gender fromKorean(String korean) {
        return switch (korean) {
            case "남" -> M;
            case "여" -> F;
            default -> throw new IllegalStateException("남/여 로 입력하세요: " + korean);
        };
    }
}
