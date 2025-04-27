package com.backend.server.api.common.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonSignInRequest {
    private String studentNumber;
    private String password;
}
