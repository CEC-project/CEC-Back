package com.backend.server.api.common.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonSignInRequest {
    @Schema(defaultValue = "202300003")
    private String studentNumber;

    @Schema(defaultValue = "asdf1234!")
    private String password;
}
