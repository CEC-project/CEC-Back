package com.backend.server.api.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonResponse<T> {
    private final String status;
    private final String message;
    private final T data;

    public static <T> CommonResponse<T> success(String message, T data) {
        return new CommonResponse<>("success", message, data);
    }

    public static <T> CommonResponse<T> fail(String message) {
        return new CommonResponse<>("fail", message, null);
    }
}