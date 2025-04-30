package com.backend.server.api.common.controller;

import com.backend.server.api.common.dto.ApiResponse;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CommonExceptionController {

    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<Object> handleRuntimeException(HttpServletResponse res, RuntimeException ex) {
        res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        log.error(ex.getMessage(), ex);
        return ApiResponse.fail(getTimeString() + ex.getMessage());
    }

    @ExceptionHandler(PersistenceException.class)
    public ApiResponse<Object> handlePersistenceException(HttpServletResponse res, PersistenceException ex) {
        res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        log.error(ex.getMessage(), ex);
        return ApiResponse.fail(getTimeString() + "DB가 sql 을 처리하는중 오류가 발생했습니다.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<Object> handleIllegalArgumentException(HttpServletResponse res, IllegalArgumentException ex) {
        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        log.error(ex.getMessage(), ex);
        return ApiResponse.fail(getTimeString() + ex.getMessage());
    }

    private String getTimeString() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX --- ");
        return sdf.format(cal.getTime());
    }

}
