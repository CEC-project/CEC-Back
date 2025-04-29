package com.backend.server.api.common.controller;

import com.backend.server.api.common.dto.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CommonExceptionController {

    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<Object> handleRuntimeException(HttpServletResponse res, RuntimeException ex) {
        res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return ApiResponse.fail(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<Object> handleIllegalArgumentException(HttpServletResponse res, IllegalArgumentException ex) {
        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return ApiResponse.fail(ex.getMessage());
    }

}
