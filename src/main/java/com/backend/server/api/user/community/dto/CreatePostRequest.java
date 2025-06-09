// CreatePostRequest.java
package com.backend.server.api.user.community.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreatePostRequest {
    private String title;
    private String content;
    private Long categoryId;
    private List<String> attachments;
    // 닉네임은 LoginUser 정보에서 가져와 서비스에서 설정하므로 요청 본문에는 포함하지 않습니다.
}
