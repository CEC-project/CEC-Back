package com.backend.server.model.repository.cookie;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.RequestScope;

@Repository
@RequestScope
@RequiredArgsConstructor
public class CookieRepository {
    private final HttpServletResponse response;

    public void setCookie(ResponseCookie cookie) {
        response.addHeader("Set-Cookie", cookie.toString());
    }
}
