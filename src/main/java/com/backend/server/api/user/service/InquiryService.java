package com.backend.server.api.user.service;


import com.backend.server.api.user.dto.inquiry.InquiryRequest;
import com.backend.server.model.entity.Inquiry;
import com.backend.server.model.repository.InquiryRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;

    public Long createInquiry(InquiryRequest request, Long currentUserId) {
        Inquiry inquiry = Inquiry.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .attachmentUrl(request.getAttachmentUrl())
                .authorId(currentUserId)
                .inquiryTypeIds(request.getInquiryTypeIds())
                .build();

        Inquiry savedInquiry = inquiryRepository.save(inquiry);
        return savedInquiry.getId();
    }
}