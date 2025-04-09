package cec.backend.user.dto.test;

import cec.backend.core.model.entity.Test;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CreateTestRequest {
    private String name;

    public Test toEntity() {
        return Test.builder().name(name).build();
    }
}
