package com.backend.server.api.admin.inquiry.dto;

import com.backend.server.api.admin.user.dto.AdminUserResponse;
import com.backend.server.model.entity.Inquiry;
import com.backend.server.model.entity.InquiryAnswer;
import com.backend.server.model.entity.Professor;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.AnswerStatus;
import com.backend.server.model.entity.enums.InquiryType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;
import lombok.Getter;

@Getter
public class AdminInquiryResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final String attachmentUrl;
    private final InquiryType inquiryType;
    private final AnswerStatus answerStatus;
    private final LocalDateTime createdAt;

    private final AdminUserResponse author;
    private final List<AdminInquiryAnswerResponse> answers;

    public AdminInquiryResponse(
            Inquiry inquiry,
            List<InquiryAnswer> answers,
            List<User> responders,
            User author,
            Professor professor) {
        this.id = inquiry.getId();
        this.title = inquiry.getTitle();
        this.content = inquiry.getContent();
        this.createdAt = inquiry.getCreatedAt();
        this.attachmentUrl = inquiry.getAttachmentUrl();
        this.inquiryType = inquiry.getType();
        this.answerStatus = inquiry.getStatus();
        this.author = new AdminUserResponse(author, professor);
        this.answers = IntStream.range(0, answers.size())
                .mapToObj((i) -> new AdminInquiryAnswerResponse(
                        answers.get(i),
                        responders.get(i)))
                .toList();
    }
}