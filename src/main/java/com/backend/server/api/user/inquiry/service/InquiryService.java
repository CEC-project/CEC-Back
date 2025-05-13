package com.backend.server.api.user.inquiry.service;


import com.backend.server.api.user.inquiry.dto.InquiryRequest;
import com.backend.server.model.entity.Inquiry;
import com.backend.server.model.repository.InquiryRepository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;

    @Transactional
    public Long createInquiry(InquiryRequest request, Long currentUserId) {

        Inquiry inquiry = Inquiry.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .attachmentUrl(request.getAttachmentUrl())
                .authorId(currentUserId)
                .type(request.getType())
                .build();

        Inquiry savedInquiry = inquiryRepository.save(inquiry);
        return savedInquiry.getId();
    }
}