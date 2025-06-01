// UpdatePostRequest.java
package com.backend.server.api.user.community.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePostRequest {
    private String title;
    private String content;
    private String type;
    private Long communityTypeId;
    // 닉네임은 수정 시 변경되지 않거나 별도 로직이 필요할 수 있으므로 여기서는 포함하지 않습니다.
}
