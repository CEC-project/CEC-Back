// UpdatePostRequest.java
package com.backend.server.api.user.community.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdatePostRequest {
    private String title;
    private String content;
    private Long categotyId;
    private List<String> attachments;
    // 닉네임은 수정 시 변경되지 않거나 별도 로직이 필요할 수 있으므로 여기서는 포함하지 않습니다.
}
