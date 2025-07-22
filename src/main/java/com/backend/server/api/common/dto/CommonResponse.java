package com.backend.server.api.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse<T> {
    private String status;
    private String message;
    private T data;

    public static <T> CommonResponse<T> success(String message, T data) {
        return new CommonResponse<>("success", message, data);
    }

    public static <T> CommonResponse<T> fail(String message) {
        return new CommonResponse<>("fail", message, null);
    }
}