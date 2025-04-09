package cec.backend.user.dto.test;

import cec.backend.core.model.entity.Test;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TestResponse {
    private Long id;
    private String name;

    public TestResponse(Test testEntity) {
        id = testEntity.getId();
        name = testEntity.getName();
    }
}
