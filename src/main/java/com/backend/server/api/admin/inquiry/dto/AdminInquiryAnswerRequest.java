package com.backend.server.api.admin.inquiry.dto;

import com.backend.server.model.entity.Inquiry;
import com.backend.server.model.entity.InquiryAnswer;
import com.backend.server.model.entity.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminInquiryAnswerRequest {
    @NotEmpty(message = "답변 내용이 비어 있습니다.")
    @Size(min = 1, max = 1000, message = "답변 내용이 길이 제한을 벗어납니다.")
    String content;

    public InquiryAnswer toEntity(User responder, Inquiry inquiry) {
        return InquiryAnswer.builder()
                .inquiry(inquiry)
                .responder(responder)
                .content(content)
                .build();
    }
}