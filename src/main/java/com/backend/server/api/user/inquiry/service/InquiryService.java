package com.backend.server.api.user.inquiry.service;


import com.backend.server.api.user.inquiry.dto.InquiryRequest;
import com.backend.server.model.entity.Inquiry;
import com.backend.server.model.entity.InquiryType;
import com.backend.server.model.repository.InquiryRepository;

import com.backend.server.model.repository.InquiryTypeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final InquiryTypeRepository inquiryTypeRepository;

    @Transactional
    public Long createInquiry(InquiryRequest request, Long currentUserId) {
        List<InquiryType> inquiryTypes = inquiryTypeRepository.findAllById(request.getInquiryTypeIds());

        Inquiry inquiry = Inquiry.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .attachmentUrl(request.getAttachmentUrl())
                .authorId(currentUserId)
                .inquiryTypes(inquiryTypes)
                .build();

        Inquiry savedInquiry = inquiryRepository.save(inquiry);
        return savedInquiry.getId();
    }
}