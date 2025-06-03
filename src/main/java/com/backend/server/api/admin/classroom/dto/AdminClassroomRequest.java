package com.backend.server.api.admin.classroom.dto;

import com.backend.server.model.entity.User;
import com.backend.server.model.entity.classroom.Classroom;
import com.backend.server.model.entity.enums.Status;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminClassroomRequest {

    @NotEmpty
    @Size(max = 20, message = "강의실 이름은 20자 이하여야 합니다.")
    private String name;

    @NotEmpty
    @Size(max = 20, message = "설명은 20자 이하여야 합니다.")
    private String description;

    @NotEmpty
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "운영 시작 시간은 HH:mm 형식이어야 합니다.")
    private String startTime;

    @NotEmpty
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "운영 종료 시간은 HH:mm 형식이어야 합니다.")
    private String endTime;

    @Size(max = 255, message = "이미지 URL은 255자 이하여야 합니다.")
    @Pattern(regexp = "^https://.*$", message = "S3 도메인의 URL만 허용됩니다.")
    private String attachment;

    @NotNull
    private Long managerId;

    public LocalTime parseStartTime() {
        return LocalTime.parse(startTime);
    }

    public LocalTime parseEndTime() {
        return LocalTime.parse(endTime);
    }

    public Classroom toEntity(User manager) {
        return Classroom.builder()
                .name(name)
                .location(description)
                .startTime(parseStartTime())
                .endTime(parseEndTime())
                .manager(manager)
                .status(Status.AVAILABLE)
                .attachment(attachment)
                .build();
    }
}