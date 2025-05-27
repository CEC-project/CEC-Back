package com.backend.server.api.admin.inquiry.dto;

import com.backend.server.model.entity.InquiryAnswer;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.Role;
import lombok.Getter;

@Getter
public class AdminInquiryAnswerResponse {

    private final Long id;
    private final String content;
    private final String attachmentUrl;
    private final String responderName;
    private final Long responderId;
    private final Role responderRole;

    public AdminInquiryAnswerResponse(InquiryAnswer answer, User responder) {
        id = answer.getId();
        content = answer.getContent();
        attachmentUrl = answer.getAttachmentUrl();
        responderName = responder.getName();
        responderId = responder.getId();
        responderRole = responder.getRole();
    }
}