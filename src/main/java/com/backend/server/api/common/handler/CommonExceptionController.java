package com.backend.server.api.common.handler;

import com.backend.server.api.common.dto.ApiResponse;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CommonExceptionController {

    @ExceptionHandler(Exception.class)
    public ApiResponse<Object> handleRuntimeException(HttpServletResponse res, Exception ex) {
        res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        log.error(ex.getMessage(), ex);
        return ApiResponse.fail(getTimeString() + ex.getMessage());
    }

    @ExceptionHandler({MethodValidationException.class, ConstraintViolationException.class})
    public ApiResponse<Object> handlePersistenceException(HttpServletResponse res, RuntimeException ex) {
        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        log.error(ex.getMessage(), ex);
        return ApiResponse.fail(getTimeString() + "DTO 의 제약조건에 맞지 않은 값입니다." + ex.getMessage());
    }

    @ExceptionHandler(PersistenceException.class)
    public ApiResponse<Object> handlePersistenceException(HttpServletResponse res, PersistenceException ex) {
        res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        log.error(ex.getMessage(), ex);
        return ApiResponse.fail(getTimeString() + "DB가 sql 을 처리하는중 오류가 발생했습니다.");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ApiResponse<Object> handleDataIntegrityViolationException(HttpServletResponse res,
            DataIntegrityViolationException ex) {
        res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        log.error(ex.getMessage(), ex);
        return ApiResponse.fail(getTimeString() + "DB의 Nullable, Unique 등 제약조건을 위반했습니다.");
    }

    @ExceptionHandler(JwtException.class)
    public ApiResponse<Object> handleJwtException(HttpServletResponse res, JwtException ex) {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        log.error(ex.getMessage(), ex);
        return ApiResponse.fail(getTimeString() + ex.getMessage());
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
